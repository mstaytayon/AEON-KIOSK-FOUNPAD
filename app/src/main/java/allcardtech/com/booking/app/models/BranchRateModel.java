package allcardtech.com.booking.app.models;


public class BranchRateModel {
    private int id;
    private String ProductID;
    private String Description;
    private String Minutes;
    private double Rate;


    public int getID() {
        return id;
    }
    public void setID(int id) {
        this.id = id;
    }

    public String getProductID() {
        return ProductID;
    }
    public void setProductID(String ProductID) {
        this.ProductID = ProductID;
    }

    public String getDescription() {
        return Description;
    }
    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getMinutes() {
        return Minutes;
    }
    public void setMinutes(String Minutes) {
        this.Minutes = Minutes;
    }

    public double getRate() {
        return Rate;
    }
    public void setRate(double Rate) {
        this.Rate = Rate;
    }


}
