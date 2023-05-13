package com.example.mos3if;

public class Model {
    int image  ;
    String title , step1 , step2 , step3 , step4 , step5;
//    String description;


    public Model(int image ,String title, String step1 , String step2 , String step3 , String step4 , String step5 ){
        this.image=image;
        this.title=title;
        this.step1= step1;
        this.step2= step2;
        this.step3= step3;
        this.step4= step4;
        this.step5= step5;


    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStep1() {
        return step1;
    }

    public void setStep1(String step1) {
        this.step1 = step1;
    }

    public String getStep2() {
        return step2;
    }

    public void setStep2(String step2) {
        this.step2 = step2;
    }

    public String getStep3() {
        return step3;
    }

    public void setStep3(String step3) {
        this.step3 = step3;
    }

    public String getStep4() {
        return step4;
    }

    public void setStep4(String step4) {
        this.step4 = step4;
    }

    public String getStep5() {
        return step5;
    }

    public void setStep5(String step5) {
        this.step5 = step5;
    }
}
