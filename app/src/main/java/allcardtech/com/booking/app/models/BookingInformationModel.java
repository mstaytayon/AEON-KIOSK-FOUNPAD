package allcardtech.com.booking.app.models;


import java.io.Serializable;


public class BookingInformationModel implements Serializable {

    private String referenceNumber,  customerName,  contactNumber,  emailAddress, playTimeHours,cardNumber,transactionDate,branchCode,guardianName,customerFirstName,customerLastName;
    private int id, customerID,socksXS, socksSmall, socksMedium, socksLarge,infantCount, childrenCount, adultCount, additionalCount,referenceID;
    private double PlayTimeRate, additionalAdultAmount, socksAmount, totalAmount;

    public int getid() {
        return id;
    }
    public void setid(int id) {
        this.id = id;
    }

    //region Customer Details
    public int getReferenceID() {
        return referenceID;
    }
    public void setReferenceID(int referenceID) {
        this.referenceID = referenceID;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public int getCustomerID() {
        return customerID;
    }
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCardNumber() {
        return cardNumber;
    }
    public void setCardNumber(String CardNumber) {
        this.cardNumber = CardNumber;
    }

    public String getGuardianName() {
        return guardianName;
    }
    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }
    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }
    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactNumber() {
        return contactNumber;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPlayTimeHours() {
        return playTimeHours;
    }
    public void setPlayTimeHours(String playTimeHours) {
        this.playTimeHours = playTimeHours;
    }
    //endregion

    //region SocksSizes
    public int getSocksXS() {
        return socksXS;
    }
    public void setSocksXS(int socksXS) {
        this.socksXS = socksXS;
    }

    public int getSocksSmall() {
        return socksSmall;
    }
    public void setSocksSmall(int socksSmall) {
        this.socksSmall = socksSmall;
    }

    public int getSocksMedium() {
        return socksMedium;
    }
    public void setSocksMedium(int socksMedium) {
        this.socksMedium = socksMedium;
    }

    public int getSocksLarge() {
        return socksLarge;
    }
    public void setSocksLarge(int socksLarge) {
        this.socksLarge = socksLarge;
    }
    //endregion

    //region Description
    public int getChildrenCount() {
        return childrenCount;
    }
    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }

    public int getInfantCount() {
        return infantCount;
    }
    public void setInfantCount(int infantCount) {
        this.infantCount = infantCount;
    }

    public int getAdultCount() {
        return adultCount;
    }
    public void setAdultCount(int adultCount) {
        this.adultCount = adultCount;
    }

    public int getAdditionalCount() {
        return additionalCount;
    }
    public void setAdditionalCount(int additionalCount) {
        this.additionalCount = additionalCount;
    }
    //endregion

    //region Rates
    public double getPlayTimeRate() {
        return PlayTimeRate;
    }
    public void setPlayTimeRate(double PlayTimeRate) {
        this.PlayTimeRate = PlayTimeRate;
    }

    public double getAdditionalAdultAmount() {
        return additionalAdultAmount;
    }
    public void setAdditionalAdultAmount(double additionalAdult) {
        this.additionalAdultAmount = additionalAdult;
    }

    public double getSocksAmount() {
        return socksAmount;
    }
    public void setSocksAmount(double socksAmount) {
        this.socksAmount = socksAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTransactionDateTime() {
        return transactionDate;
    }
    public void setTransactionDateTime(String TransactionDate) {
        this.transactionDate = TransactionDate;
    }

    public String getBranchCode() {
        return branchCode;
    }
    public void setBranchCode (String branchCode) {
        this.branchCode = branchCode;
    }

    //endregion

}

