package com.example.inventorystore2;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private int total = 0;
    private HashMap<String, Integer> itemsPriceMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeItemsPriceMap();

        Button showItemsButton = findViewById(R.id.show_items_button);
        Button buyItemsButton = findViewById(R.id.buy_items_button);
        Button addItemButton = findViewById(R.id.add_item_button);
        Button showTotalButton = findViewById(R.id.show_total_button);
        Button generateBillButton = findViewById(R.id.generate_bill_button);

        showItemsButton.setOnClickListener(v -> showItems());
        buyItemsButton.setOnClickListener(v -> buyItems());
        addItemButton.setOnClickListener(v -> addItem());
        showTotalButton.setOnClickListener(v -> showTotal());
        generateBillButton.setOnClickListener(v -> generateBill());
    }

    private void initializeItemsPriceMap() {
        itemsPriceMap.put("Blood-pressure Medicines", 50);
        itemsPriceMap.put("Allergic Medicines", 60);
        itemsPriceMap.put("Diabetics Medicines", 70);
        itemsPriceMap.put("Painkillers", 40);
        itemsPriceMap.put("Injections", 200);
        itemsPriceMap.put("Perfumes", 250);
        itemsPriceMap.put("Body Spray", 160);
        itemsPriceMap.put("Shampoo", 270);
        itemsPriceMap.put("Soaps", 60);
        itemsPriceMap.put("Hair colour", 150);
        itemsPriceMap.put("Bread", 80);
        itemsPriceMap.put("Eggs", 15);
        itemsPriceMap.put("Beans", 50);
        itemsPriceMap.put("Meat", 700);
        itemsPriceMap.put("Frozen food", 300);
    }

    private void showItems() {
        StringBuilder itemsList = new StringBuilder();
        for (Map.Entry<String, Integer> entry : itemsPriceMap.entrySet()) {
            itemsList.append(entry.getKey()).append(" - $").append(entry.getValue()).append("\n");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Available Items")
                .setMessage(itemsList.toString())
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }

    private void buyItems() {
        String[] categories = {"Medicine", "Cosmetics", "Grocery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Category")
                .setItems(categories, (dialog, which) -> showItemsDialog(categories[which]))
                .show();
    }

    private void showItemsDialog(String category) {
        String[] items;
        switch (category) {
            case "Medicine":
                items = new String[]{"Blood-pressure Medicines", "Allergic Medicines", "Diabetics Medicines", "Painkillers", "Injections"};
                break;
            case "Cosmetics":
                items = new String[]{"Perfumes", "Body Spray", "Shampoo", "Soaps", "Hair colour"};
                break;
            case "Grocery":
                items = new String[]{"Bread", "Eggs", "Beans", "Meat", "Frozen food"};
                break;
            default:
                items = new String[]{};
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Item")
                .setItems(items, (dialog, which) -> askQuantity(items[which]))
                .show();
    }

    private void askQuantity(String item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Quantity");

        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String quantityStr = input.getText().toString();
            if (!quantityStr.isEmpty()) {
                int quantity = Integer.parseInt(quantityStr);
                calculateTotal(item, quantity);
            } else {
                Toast.makeText(MainActivity.this, "Quantity cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void calculateTotal(String item, int quantity) {
        int price = itemsPriceMap.get(item);
        int totalItemPrice = price * quantity;
        total += totalItemPrice;
        Toast.makeText(this, "Added " + item + " x" + quantity + " to cart. Item Total: " + totalItemPrice, Toast.LENGTH_LONG).show();
    }

    private void addItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Item");

        View view = getLayoutInflater().inflate(R.layout.dialog_quantity, null);
        builder.setView(view);

        EditText itemNameInput = view.findViewById(R.id.item_name_input);
        EditText itemPriceInput = view.findViewById(R.id.item_price_input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String itemName = itemNameInput.getText().toString();
            String itemPriceStr = itemPriceInput.getText().toString();
            if (!itemName.isEmpty() && !itemPriceStr.isEmpty()) {
                int itemPrice = Integer.parseInt(itemPriceStr);
                itemsPriceMap.put(itemName, itemPrice);
                Toast.makeText(MainActivity.this, "Item added: " + itemName + " - $" + itemPrice, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Item name and price cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showTotal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Total: $" + total)
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }

    private void generateBill() {
        StringBuilder bill = new StringBuilder();
        bill.append("Bill:\n");
        for (Map.Entry<String, Integer> entry : itemsPriceMap.entrySet()) {
            bill.append(entry.getKey()).append(" - $").append(entry.getValue()).append("\n");
        }
        bill.append("Total: $").append(total);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bill")
                .setMessage(bill.toString())
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }
}
