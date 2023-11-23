# Elasticsearch

- [Elasticsearch](https://www.elastic.co/guide/cn/elasticsearch/guide/current/index.html)
- [十九种Elasticsearch字符串搜索方式终极介绍](https://zhuanlan.zhihu.com/p/137575167)
- [Elasticsearch入门与进阶](https://blog.csdn.net/Topdandan/article/details/81436141)
- [RestHighLevelClient查询](https://z.itpub.net/article/detail/A7B79869961FB96969AADEA98959D9FC)
- [Elasticsearch聚合结果不精确](https://blog.csdn.net/laoyang360/article/details/107133008)



查询最大条数设置，默认查询10000条

```
PUT http://{ip}:{port}/{index}/_settings

{
  "settings": {
    "index": {
      "max_result_window": 20000
    }
  }
}
```

