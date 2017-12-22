package com.larditrans.model;

import java.lang.reflect.Field;
import java.util.*;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.toList;

/**
 * Created by sunny on 19.12.17
 */
public class Holder {

    private String[] nameSamples = {"Max", "Peter", "Anna", "Tom", "Bob", "Sara", "John", "Jack", "Mick", "Julia", "Hope"};
    private String[] lastNameSamples = {"Black", "Cruise", "Chang", "Lee", "Woo", "Simpson", "Niro", "Smith"};
    private String[] roadSamples = {"Park", "Mill", "River", "Mountain", "Main", "East", "Lincoln", "Grant", "Roosevelt", "Baker"};
    private String[] citySamples = {"London", "Bristol", "York", "Edinburgh", "Dublin", "Glasgow", "Liverpool", "Manchester"};
    private String[] domainSamples = {"google.com", "mail.ru", "ukr.net", "yahoo.net", "i.am", "f.tv"};
    private String[] strSamples = {"str", "ave", "lane", "sqr"};

    private Map<Integer, Record> map;
    private final List<String> fieldNames;

    public Holder() {
        Random r = new Random();
        map = new HashMap<Integer, Record>();
        for (int i = 0; i < 50; i++) {
            Record rec = new Record();
            rec.setActive(r.nextBoolean());
            rec.setId(i);
            rec.setAge(r.nextInt(100) + 10);
            rec.setName(nameSamples[r.nextInt(nameSamples.length)] + " " + lastNameSamples[r.nextInt(lastNameSamples.length)]);
            rec.setLogin(rec.getName().replace(" ", "_").toLowerCase() + i * r.nextInt(100));
            rec.setAddress(roadSamples[r.nextInt(roadSamples.length)] + " " + strSamples[r.nextInt(strSamples.length)] + ", " +
                    (r.nextInt(1000) + 10) + ", " + citySamples[r.nextInt(citySamples.length)]);
            rec.setDate(new Date(r.nextLong() / 1000000));
            rec.setEmail(rec.getLogin() + "@" + domainSamples[r.nextInt(domainSamples.length)]);
            map.put(i, rec);
        }
        map.values().stream().forEach(System.out::println);
        Field[] fields = Record.class.getDeclaredFields();
        fieldNames = Arrays.stream(fields).filter(field -> !field.isAnnotationPresent(Hide.class)).map(f -> f.getName()).collect(toList());
    }

    public List<String> getFieldNames() {
        return fieldNames;
    }

    public List<Record> findAll() {
        return map.values().stream().collect(toList());
    }

    public Page<Record> find(boolean ageOn, int ageFrom, int ageTo, Date dateFrom, Date dateTo, boolean periodOn,
                             String whereSearch, String search, Integer page, Integer perpage,
                             boolean active, boolean activeOn, String sort, String sortDir) {
        boolean searchOff = search == null || search.equals("");
        boolean sortDirection = sortDir.toLowerCase().equals("up");
        String searchLC = search.toLowerCase();
        List<Record> origin = map.values().stream()
                .sorted(foo(sort, sortDirection)
//                        (sortDir.equals("up")) ? Comparator.comparing(Record::getId) : Comparator.comparing(Record::getId).reversed()

                )
                .filter(v -> {
                    String str = null;
                    switch (whereSearch) {
                        case "login" : str = v.getLogin(); break;
                        case "name" : str = v.getName(); break;
                        case "address" : str = v.getAddress(); break;
                        case "email" : str = v.getEmail(); break;
                    }
                    return searchOff || !searchOff && str.toLowerCase().contains(searchLC);
                })
                .filter(v -> !periodOn || periodOn && v.getDate().before(dateTo) && v.getDate().after(dateFrom))
                .filter(v -> !activeOn || activeOn && v.isActive() == active )
                .filter(v -> !ageOn || ageOn && v.getAge() >= ageFrom && v.getAge() < ageTo)
                .collect(toList());
        int pages = (int) Math.ceil(origin.size() / (double) perpage);
        if (pages == 0) {
            page = 0;
        }
        if (page > pages) {
            page = pages - 1;
        }
        Page<Record> res = new Page<>();
        res.setPages(pages);
        res.setPage(page);
        res.setPerpage(perpage);
        res.setCount(origin.size());
        int from = page * perpage;
        int to = from + perpage;
        List<Record> list = new ArrayList<>();
        for (int i = 0; i < origin.size(); i++) {
            if (i >= from && i < to) {
                list.add(origin.get(i));
            }
        }
//        List<Record> list = map.values().stream().filter(r -> r.getId() >= from && r.getId() < to).collect(toList());
        res.setList(list);
        return res;
    }

    private Comparator<? super Record> foo(String sort, boolean sortDir) { // true: up, false: down
        switch (sort) {
            case "id": return (sortDir) ? comparing(Record::getId) : comparing(Record::getId).reversed();
            case "name": return (sortDir) ? comparing(Record::getName) : comparing(Record::getName).reversed();
            case "login": return (sortDir) ? comparing(Record::getLogin) : comparing(Record::getLogin).reversed();
            case "age": return (sortDir) ? comparing(Record::getAge) : comparing(Record::getAge).reversed();
            case "date": return (sortDir) ? comparing(Record::getDate) : comparing(Record::getDate).reversed();
            case "email": return (sortDir) ? comparing(Record::getEmail) : comparing(Record::getEmail).reversed();
            case "address": return (sortDir) ? comparing(Record::getAddress) : comparing(Record::getAddress).reversed();
            case "active": return (sortDir) ? comparing(Record::isActive) : comparing(Record::isActive).reversed();
        }

        return null;
    }

    public Record find(int id) {
        return map.get(id);
    }

    public void save(Record record) {
//        record.setDate(map.get(record.getId()).getDate());
        map.put(record.getId(), record);
    }
}
