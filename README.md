# feature-toggle-demo
feature toggle demo app


## Start Config Server
```bash
$ spring cloud configserver
```

### Test Config Server
```bash
$ http :8888/feature-toggle-demo/deafult
```

```bash
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Date: Fri, 05 Apr 2019 21:59:48 GMT
Transfer-Encoding: chunked

{
    "label": null,
    "name": "feature-toggle-demo",
    "profiles": [
        "deafult"
    ],
    "propertySources": [
        {
            "name": "file:///Users/marcelwidmer/dev/git/feature-toggle-demo/config-repo/feature-toggle-demo.yml",
            "source": {
                "app.feature": "feature1",
                "logging.pattern.console": "%clr(%d{yy-MM-dd E HH:mm:ss.SSS}){blue} %clr(%-5p) %clr(${PID}){faint} %clr([${spring.zipkin.service.name:${spring.application.name:-}},){magenta}%clr(%X{X-B3-TraceId:-},){yellow}%clr(%X{X-B3-SpanId:-},){cyan}%clr(,%X{X-Span-Export:-}]){blue}%clr([%8.15t]){cyan} %clr(%-40.40logger{0}){blue} %clr(:){red} %clr(%m){faint}%n",
                "server.port": 8081
            }
        }
    ],
    "state": null,
    "version": "7f7424704be2f49b91ac56a251a2ab696cc7d85a"
}
```

## Test Feature Toggle
```bash
$ http :8081/api
```
Check if there the _chuck-norris_ link.

```bash
HTTP/1.1 200
Content-Type: application/hal+json;charset=UTF-8
Date: Fri, 05 Apr 2019 21:53:29 GMT
Transfer-Encoding: chunked

{
    "_links": {
        "chuck-norris": {
            "href": "http://api.icndb.com/jokes/random"
        },
        "customer": {
            "href": "http://localhost:8081/api/customer"
        },
        "greeting": {
            "href": "http://localhost:8081/api/greeting"
        },
        "self": {
            "href": "http://localhost:8081/api"
        }
    }
```
