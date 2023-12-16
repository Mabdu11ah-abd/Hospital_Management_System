package com.example.hospital_management_system;

import java.io.Serializable;

public class Billing implements Serializable {
    private double Dueamount=0;
    public void GenerateInvoice(Patient p)
    {
    System.out.println(p);
    System.out.println("Due Amount " +Dueamount);
    }
    public void UpdatePayment(double Newamount)
    { 
     Dueamount+=Newamount;
    }
    public void finalPayment(Patient p)
    {
        double bedcost = p.getBedUsed().getDaysOccupied()*1500;//flat rate of 1500 rps for every day that the bed is used :
        Dueamount+=bedcost;
    
    }
    public double getDueamount() {
        return Dueamount;
    }

    public void setDueamount(double dueamount) {
        Dueamount = dueamount;
    }

}
