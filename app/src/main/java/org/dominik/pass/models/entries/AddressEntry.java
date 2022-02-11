package org.dominik.pass.models.entries;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.dominik.pass.utils.ApiLocalDateDeserializer;

import java.time.LocalDate;

public final class AddressEntry {
  private String entryTitle;
  private String firstName;
  private String middleName;
  private String lastName;
  @JsonDeserialize(using = ApiLocalDateDeserializer.class) private LocalDate birthday;
  private String company;
  private String addressOne;
  private String addressTwo;
  private String city;
  private String country;
  private String state;
  private String email;
  private String phone;
  private String mobilePhone;
  private String notes;

  public AddressEntry() {}

  public static Builder builder() {
    return new Builder();
  }

  public String getEntryTitle() {
    return entryTitle;
  }

  public void setEntryTitle(String entryTitle) {
    this.entryTitle = entryTitle;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getAddressOne() {
    return addressOne;
  }

  public void setAddressOne(String addressOne) {
    this.addressOne = addressOne;
  }

  public String getAddressTwo() {
    return addressTwo;
  }

  public void setAddressTwo(String addressTwo) {
    this.addressTwo = addressTwo;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  @Override
  public String toString() {
    return "AddressEntry{" +
      "entryTitle='" + entryTitle + '\'' +
      ", firstName='" + firstName + '\'' +
      ", middleName='" + middleName + '\'' +
      ", lastName='" + lastName + '\'' +
      ", birthday=" + birthday +
      ", company='" + company + '\'' +
      ", addressOne='" + addressOne + '\'' +
      ", addressTwo='" + addressTwo + '\'' +
      ", city='" + city + '\'' +
      ", country='" + country + '\'' +
      ", state='" + state + '\'' +
      ", email='" + email + '\'' +
      ", phone='" + phone + '\'' +
      ", mobilePhone='" + mobilePhone + '\'' +
      ", notes='" + notes + '\'' +
      '}';
  }

  public static final class Builder {
    private String entryTitle;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate birthday;
    private String company;
    private String addressOne;
    private String addressTwo;
    private String city;
    private String country;
    private String state;
    private String email;
    private String phone;
    private String mobilePhone;
    private String notes;

    public Builder setEntryTitle(String entryTitle) {
      this.entryTitle = entryTitle;
      return this;
    }

    public Builder setFirstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public Builder setMiddleName(String middleName) {
      this.middleName = middleName;
      return this;
    }

    public Builder setLastname(String lastName) {
      this.lastName = lastName;
      return this;
    }

    public Builder setBirthday(LocalDate birthday) {
      this.birthday = birthday;
      return this;
    }

    public Builder setCompany(String company) {
      this.company = company;
      return this;
    }

    public Builder setAddressOne(String addressOne) {
      this.addressOne = addressOne;
      return this;
    }

    public Builder setAddressTwo(String addressTwo) {
      this.addressTwo = addressTwo;
      return this;
    }

    public Builder setCity(String city) {
      this.city = city;
      return this;
    }

    public Builder setCountry(String country) {
      this.country = country;
      return this;
    }

    public Builder setState(String state) {
      this.state = state;
      return this;
    }

    public Builder setEmail(String email) {
      this.email = email;
      return this;
    }

    public Builder setPhone(String phone) {
      this.phone = phone;
      return this;
    }

    public Builder setMobilePhone(String mobilePhone) {
      this.mobilePhone = mobilePhone;
      return this;
    }

    public Builder setNotes(String notes) {
      this.notes = notes;
      return this;
    }

    public AddressEntry build() {
      AddressEntry addressEntry = new AddressEntry();
      addressEntry.entryTitle = this.entryTitle;
      addressEntry.firstName = this.firstName;
      addressEntry.middleName = this.middleName;
      addressEntry.lastName = this.lastName;
      addressEntry.birthday = this.birthday;
      addressEntry.company = this.company;
      addressEntry.addressOne = this.addressOne;
      addressEntry.addressTwo = this.addressTwo;
      addressEntry.city = this.city;
      addressEntry.country = this.country;
      addressEntry.state = this.state;
      addressEntry.email = this.email;
      addressEntry.phone = this.phone;
      addressEntry.mobilePhone = this.mobilePhone;
      addressEntry.notes = this.notes;

      return addressEntry;
    }
  }
}
