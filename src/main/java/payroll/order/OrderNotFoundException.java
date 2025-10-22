package payroll.order;

class OrderNotFoundException extends  RuntimeException {
    OrderNotFoundException(Long id){
        super("Could not find de order of id -> " + id);
    }
}
