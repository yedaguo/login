package renren.io.model;

public class Msg {
    private Integer id;
    private String test01;
    private String test02;


    public String getTest01() {
        return test01;
    }

    public void setTest01(String test01) {
        this.test01 = test01;
    }

    public String getTest02() {
        return test02;
    }

    public void setTest02(String test02) {
        this.test02 = test02;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Msg(String test01, String test02) {
        this.test01 = test01;
        this.test02 = test02;
    }
    public  Msg(){
        super();
    }
}
