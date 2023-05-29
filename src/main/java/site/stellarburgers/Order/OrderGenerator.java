package site.stellarburgers.Order;

import java.util.ArrayList;

public class OrderGenerator extends IngredientHash{
    Order order;
    public static Order getOrderData(){
        ArrayList <String> ingredients = new ArrayList<>();
        ingredients.add(R2_D3_BUN);
        ingredients.add(SHELLFISH_MEAT);
        ingredients.add(MOLD_CHEESE);
        ingredients.add(SPICE_SAUSE);
        return new Order(ingredients);
    }
    public static Order emptyOrderList(){
        return new Order();
    }
    public static Order orderWithIncorrectHash(){
        ArrayList <String> ingredients = new ArrayList<>();
        ingredients.add(INCORRECT_BUN);
        ingredients.add(INCORRECT_MEAT);
        return new Order(ingredients);
    }
}
