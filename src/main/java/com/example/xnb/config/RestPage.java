package com.example.xnb.config;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RestPage<T> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    //元素内容
    private List<T> content;
    //是否有上一页
    private boolean first;
    //是否有下一页
    private boolean last;
    //当前页
    private int number;
    //当页条数
    private int numberOfElements;
    //页大小
    private int size;
    //总条数
    private long totalElements;
    //总页数
    private int totalPages;

    public RestPage() {

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public RestPage(IPage<T> page) {
        int pageCount = (int) (page.getTotal()%page.getSize()==0?page.getTotal()/page.getSize():page.getTotal()/page.getSize()+1);
        //是否有上一页
        Boolean queryResultsIsFirst = page.getCurrent()==0?false:true;
        //是否有下一页
        Boolean queryResultsIsLast = page.getCurrent()+1<pageCount ?true:false;

        this.content = page.getRecords();
        this.first = queryResultsIsFirst;
        this.last = queryResultsIsLast;
        this.number = (int) page.getCurrent();
        this.numberOfElements = page.getRecords().size();
        this.size = (int) page.getSize();
        this.totalElements = page.getTotal();
        this.totalPages = pageCount;
    }

    public RestPage(IPage page, List list) {
        int pageCount = (int) (page.getTotal()%page.getSize()==0?page.getTotal()/page.getSize():page.getTotal()/page.getSize()+1);
        //是否有上一页
        Boolean queryResultsIsFirst = page.getCurrent()==0?false:true;
        //是否有下一页
        Boolean queryResultsIsLast = page.getCurrent()+1<pageCount ?true:false;

        this.content = list;
        this.first = queryResultsIsFirst;
        this.last = queryResultsIsLast;
        this.number = (int) page.getCurrent();
        this.numberOfElements = page.getRecords().size();
        this.size = (int) page.getSize();
        this.totalElements = page.getTotal();
        this.totalPages = pageCount;
    }
}