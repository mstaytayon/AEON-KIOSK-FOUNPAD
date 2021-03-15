package allcardtech.com.booking.app.models;

public class CustomerModel extends BookingInformationModel{
    private int CustomerID,Type;
    private String FirstName,MiddleName,LastName,MobileNumber,EmailAddress,CardNumber,FullName;

    public  int getCustomerID(){
        return CustomerID;
    }
    public  void setCustomerID(int CustomerID ){
        this.CustomerID = CustomerID;
    }

    public  String getFirstName( ){
        return FirstName;
    }
    public  void setFirstName(String FirstName ){
        this.FirstName = FirstName;
    }

    public  String getMiddleName(){
        return MiddleName;
    }
    public  void setMiddleName(String MiddleName ){
        this.MiddleName = MiddleName;
    }

    public  String getFullName(){
        return FullName;
    }
    public  void setFullName(String FullName ){
        this.FullName = FullName;
    }

    public  String getLastName(){
        return LastName;
    }
    public  void setLastName(String LastName ){
        this.LastName = LastName;
    }

    public  String getMobileNumber(){
        return MobileNumber;
    }
    public  void setMobileNumber(String MobileNumber ){
        this.MobileNumber = MobileNumber;
    }

    public  String getEmailAddress(){
        return EmailAddress;
    }
    public  void setEmailAddress(String EmailAddress ){
        this.EmailAddress = EmailAddress;
    }

    public  String getCardNumber( ){
        return CardNumber;
    }
    public  void setCardNumber(String CardNumber ){
        this.CardNumber = CardNumber;
    }

    public  int getType( ){
        return Type;
    }
    public  void setType(int Type ){
        this.Type = Type;
    }

}
