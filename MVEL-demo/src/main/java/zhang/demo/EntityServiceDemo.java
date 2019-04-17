package zhang.demo;

import java.util.ArrayList;
import java.util.List;

public class EntityServiceDemo {

    public List<EntityDemo> getEntityDemos() {
        List demos = new ArrayList<EntityDemo>() {{
            add(new EntityDemo("Goods1", 1, 9.9));
            add(new EntityDemo("Goods2", 2, 120.5));
            add(new EntityDemo("Goods3", 3, 200));
            add(new EntityDemo("Goods4", 4, 99.9));
        }};
        return demos;
    }

}
