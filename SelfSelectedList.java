import java.util.ArrayList;
import java.util.List;

//-----------------------------------總自選股列表清單，裡面包含多個自選股列表--------------------------

public class SelfSelectedList {
    private List<SelfSelectedStocks> allList;       //自選股列表清單

    public SelfSelectedList() {
        allList = new ArrayList<SelfSelectedStocks>();
    }
    public void addList(String name){       //增加列表
        allList.add(new SelfSelectedStocks(name));
    }
    public void deleteList(int index){      //刪除列表
        allList.remove(index-1);
    }
    public SelfSelectedStocks getList(int index){       //取得列表
        return allList.get(index-1);
    }

    public void showList() {
        for (int i = 0; i < allList.size(); i++){
            System.out.printf("%d. %s%n", i+1, allList.get(i).getName());
        }
        System.out.println();
    }
}
