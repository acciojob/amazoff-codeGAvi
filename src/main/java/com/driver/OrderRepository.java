package com.driver;

import java.util.*;

import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository(){
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order){
        // your code here
        orderMap.put(order.getId(), order);
    }

    public void savePartner(String partnerId){
        // your code here
        // create a new partner with given partnerId and save it
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        partnerMap.put(partnerId,deliveryPartner);
    }

    public void saveOrderPartnerMap(String orderId, String partnerId){
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
            // your code here
            //add order to given partner's order list
            //increase order count of partner
            //assign partner to this order

            orderToPartnerMap.put(orderId,partnerId);

//            if(partnerToOrderMap.get(partnerId) == null){
//                // then put empty hashSet with this partnerId
//                partnerToOrderMap.put(partnerId,new HashSet<>());
//            }

            // we can write like this also f
            partnerToOrderMap.putIfAbsent(partnerId,new HashSet<>());

            // if not empty then simply add orderId in this hashSet
            partnerToOrderMap.get(partnerId).add(orderId);   // add order in hashSet  for this partnerId

            // increase order cout
            DeliveryPartner deliveryPartner = partnerMap.get(partnerId);
            deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders()+1);

            // now finally put this in partner map
            partnerMap.put(partnerId,deliveryPartner);
        }
    }

    public Order findOrderById(String orderId){
        // your code here
        if(!orderMap.containsKey(orderId)){
            return null;
        }
        return orderMap.get(orderId);
    }

    public DeliveryPartner findPartnerById(String partnerId){
        // your code here
        return partnerMap.get(partnerId);
    }

    public Integer findOrderCountByPartnerId(String partnerId){
        // your code here
        DeliveryPartner dpartner = partnerMap.get(partnerId);
        return  dpartner.getNumberOfOrders();
    }

    public List<String> findOrdersByPartnerId(String partnerId){
        // your code here

        HashSet<String> order = partnerToOrderMap.get(partnerId);
        // make a list of orders
        List<String> orderList = new ArrayList<>(order);
        return orderList;

    }

    public List<String> findAllOrders(){
        // your code here
        // return list of all orders
//        List<String> allOrders = new ArrayList<>(orderMap.keySet());
//        return allOrders;

        return new ArrayList<>(orderMap.keySet());
    }

    public Integer findCountOfUnassignedOrders(){
        // your code here
       return orderMap.size()-orderToPartnerMap.size();
    }


    public void deletePartner(String partnerId){
        // your code here
        // delete partner by ID
        HashSet<String> assignedOrderswiththisPartnerId = partnerToOrderMap.get(partnerId);

        // unassigned it from order to partner map
        for(String orderId:assignedOrderswiththisPartnerId){
            orderToPartnerMap.remove(orderId);
        }
        partnerToOrderMap.remove(partnerId);
        // also remove partner from partner map
        partnerMap.remove(partnerId);

    }

    public void deleteOrder(String orderId){
        // your code here
        // delete order by ID

        //get assigned partner for this order
        String assignedPartnerId = orderToPartnerMap.get(orderId);

        // then get setof orders with this partnerId and delete particular orderId
        partnerToOrderMap.get(assignedPartnerId).remove(orderId);

        // then  unassigned this partnerId from partner to order map
        orderToPartnerMap.remove(orderId);

        // now remove this orderId from order
        orderMap.remove(orderId);
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId){
        // your code here

        // convert input string time into integer min
        String InputTime[] = timeString.split(":");
        int hour = Integer.parseInt(InputTime[0]);
        int min = Integer.parseInt(InputTime[1]);

        int totalInputTime = hour*60+min;

        // get all orders(set of orders) assigned for this partnerId
        int leftOrder = 0;
        HashSet<String> totalOrdersId = partnerToOrderMap.get(partnerId);
        for(String orderId:totalOrdersId) {
            Order order = orderMap.get(orderId);
            int DeliveryTime = order.getDeliveryTime();   // find delivery time for each order
            if(DeliveryTime<totalInputTime){
                totalInputTime -= DeliveryTime;     // left_input_time
            }
            else {
                leftOrder++;     // if delivery time is more than left_input_time.. means it can't be delivered
            }
        }
        return leftOrder;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId){
        // your code here
        // code should return string in format HH:MM

        // get set of orders for this partnerId
        HashSet<String> listofOrders = partnerToOrderMap.get(partnerId);

        // now find delivery time of each  order and store max time
        int maxTime = 0;   // in minnutes ..... from order class
        for(String orderId:listofOrders){
            maxTime = Math.max(maxTime, orderMap.get(orderId).getDeliveryTime());
        }

        // convert integer timme into hour & minute format
        int hour = maxTime/60;
        int min = maxTime%60;

        // convert into string format HH:MM
        String timeString = String.format("%02d:%02d", hour, min);
        return timeString;
    }
}