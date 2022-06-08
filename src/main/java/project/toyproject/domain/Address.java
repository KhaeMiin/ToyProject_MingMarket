package project.toyproject.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String address; //주소
    private String detailedAddress; //상세주소

    public Address(String address, String detailedAddress) {
        this.address = address;
        this.detailedAddress = detailedAddress;
    }
}
