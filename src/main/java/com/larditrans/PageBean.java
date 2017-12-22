package com.larditrans;

import com.google.gson.Gson;
import com.larditrans.model.*;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunny on 19.12.17
 */
public class PageBean {

    private HttpServletRequest request;
    private HttpServletResponse response;

    private static Holder holder = new Holder();


    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    private String getStringParam(String name) {
        if (name != null && !name.trim().equals("")) {
            return request.getParameter(name);
        }
        return null;
    }

    private Date getDateParam(String name) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (name != null && !name.trim().equals("")) {
            String parameter = request.getParameter(name);
            Date date = null;
            try {
                date = format.parse(parameter);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }
        return null;
    }

    private Integer getIntParam(String name) {
        if (name != null && !name.trim().equals("")) {
            String param = request.getParameter(name);
            try {
                return Integer.parseInt(param);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Boolean getBooleanParam(String name) {
        if (name != null && !name.trim().equals("")) {
            String parameter = request.getParameter(name);
            return new Boolean(parameter);
        }
        return null;
    }

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String table() throws InterruptedException {
        if (!request.getMethod().toLowerCase().equals("get")) {
            return "";
        }

        Integer page = getIntParam("page");
        Integer perpage = getIntParam("perpage");
        String search = getStringParam("search");
        String sort = getStringParam("sort");
        String sortDir = getStringParam("sort_dir");
        String whereSearch = getStringParam("where_search");
        Date dateFrom = getDateParam("date_from");
        Date dateTo = getDateParam("date_to");
        Boolean active = getBooleanParam("active");
        Boolean activeOn = getBooleanParam("active_on");
        Boolean periodOn = getBooleanParam("period_on");
        Boolean ageOn = getBooleanParam("age_on");
        int ageFrom = getIntParam("age_from");
        int ageTo = getIntParam("age_to");
        Page<Record> pageView = holder.find(ageOn, ageFrom, ageTo, dateFrom, dateTo, periodOn, whereSearch, search, page, perpage, active, activeOn, sort, sortDir);

        Thread.sleep(1500);//todo  kill thw line: emulation external service work delay.

        List<Record> recordList = pageView.getList();
        List<String> fieldNames = holder.getFieldNames();

        int rows = recordList.size();
        if (recordList.size() == 0) {
            return new Gson().toJson(new Result(0, 1, 0, "", ""));
        }
        int cols = fieldNames.size();
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1' cellpadding='10' cellspacing='0'>");
        sb.append("<tr>");
        sb.append("<th>#</th>");
        for (int i = 0; i < cols; i++) {
            sb.append("<th onclick='setSort(this);'>").append(fieldNames.get(i));
            if (sort.equals(fieldNames.get(i))) {
                sb.append(" ").append(sortDir.equals("up") ? "&#x25B4;" : "&#x25BE;");
            }
            sb.append("</th>");
        }
        sb.append("</tr>");
        for (int i = 0; i < rows; i++) {
            sb.append("<tr>");
            sb.append("<td>row ").append(i).append("</td>");
            for (int j = 0; j < cols; j++) {
                sb.append("<td>");
                String name = fieldNames.get(j);
                Object obj = recordList.get(i).get(name);
                if (name.equals("id")) {
                    sb.append("<a onclick='edit(").append(obj).append(");'>").append(obj).append("</a>");
                } else if (name.equals("date")) {
                    String date = format.format(obj);
                    sb.append(date);
                } else {
                    sb.append(obj);
                }
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
        int pages = pageView.getPages();
        StringBuilder pg = new StringBuilder();

        page = pageView.getPage();
        pages = pageView.getPages();
        pg.append("<table class='paginator'><tr><td>");
        pg.append("  page ").append(page + 1);
        pg.append(" of ").append(pages).append("&nbsp;&nbsp;&nbsp;");
        if (page == 0) {
            pg.append("<b>first</b>");
        } else {
            pg.append("<a onclick='setPage(0);'>first</a>");
        }
        pg.append("&nbsp;&nbsp;&nbsp;");
        if (page == 0) {
            pg.append("&lt;&lt;");
        } else {
            pg.append("<a onclick='setPage(").append(page == 0 ? 0 : page - 1).append(");'>&lt;&lt;</a>");
        }
        pg.append("&nbsp;&nbsp;&nbsp;");
        int from = page > 5 ? page - 5 : 0;
        int to = pages - page > 5 ? page + 5 : pages - 1;
        for (int i = from; i <= to; i++) {
            if (i == page) {
                pg.append("<b>").append(i + 1).append("</b>");
            } else {
                pg.append("<a onclick='setPage(").append(i).append(");'>")
                        .append(i + 1).append("</a>");
            }
            pg.append("&nbsp;&nbsp;&nbsp;");
        }
        if (page == pages - 1 || pages == 0) {
            pg.append("&gt;&gt;");
        } else {
            pg.append("<a onclick='setPage(").append(page == pages - 1 ? pages - 1 : page + 1).append(");'>&gt;&gt;</a>");
        }
        pg.append("&nbsp;&nbsp;&nbsp;");
        if (page == pages - 1 || pages == 0) {
            pg.append("<b>last</b>");
        } else {
            pg.append("<a onclick='setPage(").append(pages - 1).append(");'>last</a>");
        }
        pg.append("&nbsp;&nbsp;&nbsp;");
        pg.append("</td>");

        pg.append("</tr></table>");

        return new Gson().toJson(new Result(pages, page, pageView.getCount(), pg.toString(), sb.toString()));
    }

    public String edit() {
//        if (!request.getMethod().toLowerCase().equals("get")) {
//            return "";
//        }
        int id = getIntParam("id");
        Record record = holder.find(id);
        Gson gson = new Gson();
        String str = gson.toJson(record);
        System.out.println("to edit = " + str);
        return str;
    }

    public String save() {
        if (!request.getMethod().toLowerCase().equals("post")) {
            return "";
        }
        Integer age = getIntParam("age");
        Integer id = getIntParam("id");
        Boolean active = getBooleanParam("active");
        Date date = getDateParam("date");
        String name = getStringParam("name");
        String address = getStringParam("address");
        String email = getStringParam("email");
        String login = getStringParam("login");
        Record record = new Record();
        record.setEmail(email);
        record.setAddress(address);
        record.setLogin(login);
        record.setDate(date);
        record.setName(name);
        record.setActive(active);
        record.setAge(age);
        record.setId(id);
        Errors errors = valiadate(record);
        System.out.println("errors = " + errors);
        if (errors.size() == 0) {
        holder.save(record);
            return "ok";
        }
        return new Gson().toJson(errors.getErrors());
    }

    private Errors valiadate(Record record) {
        Errors errors = new Errors();
        if (record.getAge() < 5 || record.getAge() > 70) errors.put("age", "age must be in range: 5 - 70");
        if (record.getName().length() < 3 || record.getName().length() > 10) errors.put("name", "name size must be in range: 3 - 10");
        if (!record.getName().matches("^[A-Z]{1}.*")) errors.put("name", "name must start with capital");
        if (!record.getEmail().contains("@")) errors.put("email", "wrong email");
        return errors;
    }

    private Date getDateTimeParam(String name) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (name != null && !name.trim().equals("")) {
            String param = request.getParameter(name);
            return new Date(param);
        }
        return null;
    }

    public String reboot() {
        holder = new Holder();
        return "";
    }


}
