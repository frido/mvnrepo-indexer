package frido.mvnrepo.indexer.core.download;

import java.util.Comparator;

public class DeepComparator implements Comparator<Runnable> {

    @Override
    public int compare(Runnable o1, Runnable o2) {
        int l1 = getDeep(o1.toString());
        int l2 = getDeep(o2.toString());
        return (-1) * (l1 - l2);
    }

    public int getDeep(String link) {
        int deep = 0;
        for (char ch : link.toCharArray()) {
            if (ch == '/') {
                deep++;
            }
        }
        return deep;
    }

}