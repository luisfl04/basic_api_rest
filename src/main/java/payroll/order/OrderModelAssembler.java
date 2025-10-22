package payroll.order;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>>{

    @Override
    public EntityModel<Order> toModel(Order order){

        EntityModel<Order> orderEntityModel = EntityModel.of(order,
                linkTo(methodOn(OrderController.class).getOne(order.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getAll()).withRel("orders")
        );

        if(order.getStatus() == StatusOrder.IN_PROGRESS){
            orderEntityModel.add(linkTo(methodOn(OrderController.class).cancelOrder(order.getId())).withRel("cancel_order"));
            orderEntityModel.add(linkTo(methodOn(OrderController.class).completeOrder(order.getId())).withRel("complete_order"));
        }

        return orderEntityModel;
    }

}
