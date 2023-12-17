package com.example.hospital_management_system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Inventory implements Serializable {
    public ArrayList<Item> getItemsinInventory() {
        return ItemsinInventory;
    }

    private ArrayList<Item> ItemsinInventory = new ArrayList<Item>(20);

    public void setItemsinInventory(ArrayList<Item> itemsinInventory) {
        ItemsinInventory = itemsinInventory;
    }

    public double BuyItems() {
        Scanner input = new Scanner(System.in);

        System.out.println("enter ID and Quantity of product you want to buy : ");
        String buyID = input.nextLine();
        if (searchItem(buyID) != -1) {
            int index = searchItem(buyID);
            int buyQuantity = input.nextInt();
            if (buyQuantity > ItemsinInventory.get(index).getQuantity())
                System.out.println("Quantity Exceeds inventory size : ");
            return ItemsinInventory.get(index).getPrice() * buyQuantity;
        } else {
            System.out.println("Invalid ID entered :");
        }
        return 0;
    }


    public void addHardcoded(Item item)
    {
        ItemsinInventory.add(item);
    }

    public void Displayinventory()
    {
        for (int i = 0; i < ItemsinInventory.size(); i++) {

            System.out.println(ItemsinInventory.get(i));
            System.out.println("\nNext Item\n");
        }
    }

    public void Addinventory() {
        Item temp = new Item();
        temp.SetItem();
        ItemsinInventory.add(temp);
    }

    public void updateinventory() {
        Scanner input = new Scanner(System.in);
        // features to add in update inventory method :
        /*
         * 1.Display menu of functions that the user can perform:
         * 2.remove Item
         * 3.update Quantity of item or price depending on choice
         */

        while (true) {
        System.out.println("1.Update Quantity Or Price :\n2.Remove Item\n3.Exit");
        int choice = input.nextInt();
        input.nextLine();
            if (choice == 1) {
                System.out.println("Enter the id of the Producat you want to search : ");
                String TempID = input.nextLine();
                // nested if else condition
                if (searchItem(TempID) != -1) {
                    ItemsinInventory.get(searchItem(TempID)).UpdateQuantity();
                } else {
                    System.out.println("item does not exist");
                }
            } else if (choice == 2) {
                System.out.println("Enter the id of the Product you want to remove : ");
                String TempID = input.nextLine();
                if (searchItem(TempID) != -1) {
                    ItemsinInventory.remove(searchItem(TempID));
                } else {
                    System.out.println("item does not exist");
                }
            }
            else if(choice==3)
            {
                return;
            }
            else 
            {
                System.out.println("Invalid input : ");
            }
        }

    }

    public int searchItem(String id) {
        int index = -1;
        for (int i = 0; i < ItemsinInventory.size(); i++) {
            if (ItemsinInventory.get(i).getItemID().equals(id)) {
                index = i;
            }
        }
        return index;
    }
}
