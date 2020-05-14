import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class AgencyImpl implements Agency {
    private Map<String, Invoice> invoiceBySerialNumber;
    private Map<LocalDate, Set<Invoice>> invoiceByDate;
    private Set<Invoice> payed;

    public AgencyImpl() {
        this.invoiceBySerialNumber = new LinkedHashMap<>();
        this.invoiceByDate = new LinkedHashMap<>();
        this.payed = new HashSet<>();
    }

    @Override
    public void create(Invoice invoice) {
        String number = invoice.getNumber();

        if (this.contains(number)) {
            throw new IllegalArgumentException();
        }

        // Add Invoices to the invoiceBySerialNumber map
        this.invoiceBySerialNumber.put(number, invoice);

        // Add Invoices to the invoiceByDate map
        LocalDate dueDate = invoice.getDueDate();
        this.invoiceByDate.putIfAbsent(dueDate, new HashSet<>());
        this.invoiceByDate.get(dueDate).add(invoice);
    }

    @Override
    public boolean contains(String number) {
        return this.invoiceBySerialNumber.containsKey(number);
    }

    @Override
    public int count() {
        return this.invoiceBySerialNumber.size();
    }

    @Override
    public void payInvoice(LocalDate dueDate) {
        Set<Invoice> invoices = this.invoiceByDate.get(dueDate);

        if (invoices == null || invoices.isEmpty()) {
            throw new IllegalArgumentException();
        }

        for (Invoice invoice : invoices) {
            invoice.setSubtotal(0);
            this.invoiceBySerialNumber.get(invoice.getNumber()).setSubtotal(0);
            payed.add(invoice);
        }

//        this.invoiceBySerialNumber.values().stream()
//                .filter(invoice -> invoice.getDueDate().isEqual(dueDate))
//                .findFirst()
//                .orElseThrow(IllegalAccessError::new);
//
//        this.invoiceBySerialNumber.entrySet()
//                .stream()
//                .filter(invoice -> invoice.getValue().getDueDate().isEqual(dueDate))
//                .forEach(invoice -> invoice.getValue().setSubtotal(0d));
    }

    @Override
    public void throwInvoice(String number) {
        Invoice invoice = this.invoiceBySerialNumber.get(number);

        if (invoice == null) {
            throw new IllegalArgumentException();
        }

        Invoice remove = this.invoiceBySerialNumber.remove(number);

        LocalDate dueDate = remove.getDueDate();
        this.invoiceByDate.get(dueDate).remove(remove);
    }

    @Override
    public void throwPayed() {
//        this.invoiceBySerialNumber = this.invoiceBySerialNumber.entrySet().stream()
//                .filter(invoiceEntry -> invoiceEntry.getValue().getSubtotal() != 0)
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, HashMap::new));

        for (Invoice invoice : payed) {
            this.invoiceBySerialNumber.remove(invoice.getNumber());
            
            LocalDate dueDate = invoice.getDueDate();
            if (this.invoiceByDate.get(dueDate) != null){
                this.invoiceByDate.remove(dueDate);
            }
        }

        this.payed.clear();

    }

    @Override
    public Iterable<Invoice> getAllInvoiceInPeriod(LocalDate startDate, LocalDate endDate) {
//        List<LocalDate> collect = this.invoiceByDate.keySet().stream().filter(invoiceDate ->
//                (invoiceDate.isAfter(startDate) && invoiceDate.isBefore(endDate))
//                        || invoiceDate.isEqual(startDate)
//                        || invoiceDate.isEqual(endDate))
//                .collect(Collectors.toList());


        return this.invoiceBySerialNumber.values()
                .stream()
                .filter(invoice -> invoice.getIssueDate().isAfter(startDate)
                        && invoice.getIssueDate().isBefore(endDate)
                        || invoice.getIssueDate().isEqual(startDate)
                        || invoice.getIssueDate().isEqual(endDate))
                .sorted(Comparator.comparing(Invoice::getIssueDate).thenComparing(Invoice::getDueDate))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Invoice> searchByNumber(String number) {
        List<Invoice> result = new ArrayList<>();

        for (Map.Entry<String, Invoice> entry : invoiceBySerialNumber.entrySet()) {
            if (entry.getKey().contains(number)) {
                result.add(entry.getValue());
            }
        }

        if (result.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return result;
    }

    @Override
    public Iterable<Invoice> throwInvoiceInPeriod(LocalDate startDate, LocalDate endDate) {
        List<Invoice> invoices = this.invoiceBySerialNumber.values().stream()
                .filter(invoice -> invoice.getDueDate().isAfter(startDate)
                        && invoice.getDueDate().isBefore(endDate))
                .collect(Collectors.toList());

        if (invoices.isEmpty()) {
            throw new IllegalArgumentException();
        }

        for (Invoice invoice : invoices) {
            this.invoiceBySerialNumber.remove(invoice.getNumber());

            LocalDate dueDate = invoice.getDueDate();
            if (this.invoiceByDate.get(dueDate) != null){
                this.invoiceByDate.remove(dueDate);
            }
        }

        return invoices;
    }

    @Override
    public Iterable<Invoice> getAllFromDepartment(Department department) {
        return this.invoiceBySerialNumber.values().stream()
                .filter(invoice -> invoice.getDepartment().ordinal() == department.ordinal())
                .sorted(Comparator.comparingDouble(Invoice::getSubtotal).reversed()
                        .thenComparing(Invoice::getIssueDate))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Invoice> getAllByCompany(String companyName) {
        return this.invoiceBySerialNumber.values().stream()
                .filter(invoice -> invoice.getCompanyName().equals(companyName))
                .sorted(Comparator.comparing(Invoice::getNumber).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void extendDeadline(LocalDate endDate, int days) {
        Set<Invoice> invoices = this.invoiceByDate.get(endDate);

        if (invoices.isEmpty()) {
            throw new IllegalArgumentException();
        }

        for (Invoice invoice : invoices) {
            invoice.setDueDate(invoice.getDueDate().plusDays(days));
        }
    }
}
