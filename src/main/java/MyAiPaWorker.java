import com.github.onblog.util.AiPaUtil;
import com.github.onblog.worker.AiPaWorker;
import org.jsoup.nodes.Document;

public class MyAiPaWorker implements AiPaWorker {
    @Override
    public String run(Document doc, AiPaUtil util) {
        //使用JSOUP进行HTML解析获取想要的div节点和属性
        //保存在数据库或本地文件中
        //新增aiPaUtil工具类可以再次请求网址
        return doc.title() + doc.body().text();
    }

    @Override
    public Boolean fail(String link) {
        //任务执行失败
        //可以记录失败网址
        //记录日志
        return false;
    }
} 