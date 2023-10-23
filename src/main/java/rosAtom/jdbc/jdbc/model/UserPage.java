package rosAtom.jdbc.jdbc.model;

import java.util.*;

public class UserPage {

    private static int ELS_PER_PAGE = 5;

    private int currentPage;

    private int startIndex;

    private int endIndex;

    private int ageFilter;

    private String nameFilter;

    private String lastnameFilter;

    private String isAdminFilter;

    public boolean isFilter() {
        return nameFilter != null || ageFilter != 0 || lastnameFilter != null || isAdminFilter != null;
    }

    public boolean isPageable() {
        return currentPage != 0;
    }

    public Map<String, Object> getCriteria() {
        Map<String, Object> cr = new HashMap<>();
        if (nameFilter != null) {
            cr.put("firstname", nameFilter);
        }
        if (lastnameFilter != null) {
            cr.put("lastname", lastnameFilter);
        }
        if (ageFilter != 0) {
            cr.put("age", ageFilter);
        }
        if (isAdminFilter != null) {
            cr.put("is_admin", Boolean.valueOf(isAdminFilter));
        }
        return cr;
    }

    public List<Object> getParams() {
        List<Object> params = new ArrayList<>();
        params.add(ageFilter);
        params.add(nameFilter);
        params.add(lastnameFilter);
        params.add(isAdminFilter);
        params.removeIf(Objects::isNull);
        params.removeIf(o -> o.equals(0));
        if (currentPage != 0) {
            params.add(ELS_PER_PAGE * (currentPage - 1) + 1);
            params.add(currentPage * ELS_PER_PAGE);
        }
        return params;
    }

}
