package payroll.order;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.antlr.v4.runtime.misc.NotNull;
import org.apache.coyote.Response;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
class OrderController {

    private final OrderRepository repository;
    private final OrderModelAssembler assembler;

    OrderController(OrderRepository orderRepository, OrderModelAssembler orderModelAssembler){
        this.repository = orderRepository;
        this.assembler = orderModelAssembler;
    }

    @GetMapping("/orders")
    CollectionModel<EntityModel<Order>> getAll(){
        List<EntityModel<Order>> orders = repository.findAll().stream().map(
                assembler::toModel
        ).collect(Collectors.toList());

        return CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).getAll()).withSelfRel());

    }

    @GetMapping("/orders/{id}")
    EntityModel<Order> getOne(@PathVariable Long id){
        Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        return assembler.toModel(order);
    }

    @PostMapping("/orders")
    ResponseEntity<EntityModel<Order>> createOrder(@RequestBody Order newOrder){
        newOrder.setStatus(StatusOrder.IN_PROGRESS);
        Order newObjectOrder = repository.save(newOrder);

        return  ResponseEntity.created(
                linkTo(methodOn(OrderController.class).getOne(newObjectOrder.getId())).toUri()
        ).body(assembler.toModel(newObjectOrder));
    }

    @DeleteMapping("/orders/{id}/cancel")
    ResponseEntity<?> cancelOrder(@PathVariable Long id){
        Order order = repository.findById(id).orElseThrow( () -> new OrderNotFoundException(id));

        if(order.getStatus() == StatusOrder.IN_PROGRESS){
            order.setStatus(StatusOrder.CANCELED);
            return  ResponseEntity.ok(assembler.toModel(repository.save(order)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed")
                        .withDetail("You can't cancel an order that is in the " + order.getStatus() + " status")
                );
    }

    @PutMapping("orders/{id}/complete")
    ResponseEntity<?> completeOrder(@PathVariable Long id){
        Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        if(order.getStatus() == StatusOrder.IN_PROGRESS){
            order.setStatus(StatusOrder.COMPLETED);
            return ResponseEntity.ok(assembler.toModel(repository.save(order)));
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withDetail("You can't complete an order that is in the " + order.getStatus() + " status")
                        .withTitle("Method not allowed")
                );
    }




}
