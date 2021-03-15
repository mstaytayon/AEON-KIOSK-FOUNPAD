package allcardtech.com.booking.app.models;

public class SocksModel extends BookingInformationModel{


    private String Description;
    private double Rate;
    private int Count;

    public  String getDescription(){
        return Description;
    }
    public  void setDescription(String Description ){
        this.Description = Description;
    }

    public  int getCount( ){
        return Count;
    }
    public  void setCount(int Count ){
        this.Count = Count;
    }

    public  double getRate(){
        return Rate;
    }
    public  void setRate(double Rate ){
        this.Rate = Rate;
    }

}
