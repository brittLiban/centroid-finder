Open Telemetry is a tool which will essentially track WHEN THE PROGRAM RUNS
-   How much time the program is spending in each method

java -javaagent:C:/Users/liban/OneDrive/Documents/Bac/opentelemetry-javaagent.jar \
-jar target/centroidfinder-1.0-SNAPSHOT-jar-with-dependencies.jar \
shortTest.mp4 ensantina_tracking.csv 5a020c 60

# 📊 Observability Setup with OpenTelemetry + Prometheus

This project uses **OpenTelemetry**, **Prometheus**, and **Docker** to collect and monitor metrics from a Java application.

---

## ⚙️ What This Setup Includes

* **Java App** instrumented with the OpenTelemetry Java Agent
* **OpenTelemetry Collector** to receive, process, and expose metrics
* **Prometheus** to scrape and store those metrics
* (Optional) **Grafana** can be added later for visualization

---

## ⚙️ How the System Works

```
[Java App]
  |
  |  (sends OTLP metrics over HTTP to port 4318)
  ↓
[OpenTelemetry Collector]
  |
  |  (exposes Prometheus-formatted metrics at port 8889)
  ↓
[Prometheus]
  |
  |  (scrapes metrics every 5s)
  ↓
[Metrics Stored + Queryable]
```

### Breakdown:

| Component                   | Role                                                   |
| --------------------------- | ------------------------------------------------------ |
| **Java App**                | Runs your code, collects metrics using OTel agent      |
| **OpenTelemetry Collector** | Ingests metrics via OTLP, exports in Prometheus format |
| **Prometheus**              | Scrapes metrics from Collector and stores them         |

---

## 🔌 Ports Used

| Port | Used By                | Purpose                                |
| ---- | ---------------------- | -------------------------------------- |
| 4318 | App → Collector        | OTLP over HTTP (default agent export)  |
| 4317 | (optional)             | OTLP over gRPC (faster, binary format) |
| 8889 | Prometheus ← Collector | Prometheus scrapes metrics here        |
| 9090 | You                    | Access Prometheus UI in the browser    |

---

## 📁 Files Included

### `prometheus.yml`

Configures Prometheus to scrape metrics from the OpenTelemetry Collector every 5 seconds.

```yaml
scrape_configs:
  - job_name: 'otel-collector'
    static_configs:
      - targets: ['otel-collector:8889']
```

---

### `otel-collector-config.yaml`

Tells the collector to:

* Receive OTLP data on ports 4318/4317
* Expose metrics in Prometheus format on port 8889

```yaml
receivers:
  otlp:
    protocols:
      grpc:
      http:

exporters:
  prometheus:
    endpoint: "0.0.0.0:8889"

service:
  pipelines:
    metrics:
      receivers: [otlp]
      exporters: [prometheus]
```

---

### `docker-compose.yml`

Starts both Prometheus and the Collector with correct ports and config file mounts.

```yaml
services:
  otel-collector:
    image: otel/opentelemetry-collector-contrib:latest
    ports:
      - "4318:4318"
      - "4317:4317"
      - "8889:8889"
    volumes:
      - ./otel-collector-config.yaml:/etc/otel-collector-config.yaml

  prometheus:
    image: prom/prometheus:latest
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
```

---

## ▶️ Running It

1. Start Prometheus + Collector:

   ```bash
   docker-compose up -d
   ```

2. Run your Java app with the agent:

   ```bash
   java -javaagent:/path/to/opentelemetry-javaagent.jar \
   -Dotel.metrics.exporter=otlp \
   -Dotel.exporter.otlp.endpoint=http://localhost:4318 \
   -Dotel.resource.attributes=service.name=centroid-finder \
   -jar target/your-app.jar [args...]
   ```

3. Visit Prometheus UI:

   ```
   http://localhost:9090
   ```

---

## 🥪 Verify

* In Prometheus, go to **Status > Targets** — make sure the target `otel-collector:8889` is **UP**
* Try entering metric names like:

  * `jvm.gc.duration`
  * `process.runtime.memory.used`
  * `queueSize`

---

## 📈 Next Step: Add Grafana

To visualize your metrics beautifully with dashboards, connect Grafana to this Prometheus instance.

Stay tuned!

---

## 🧠 Why This Works

* Your Java app sends metrics using the **OTLP format**
* The Collector receives OTLP metrics on **port 4318**
* It converts and **exposes those metrics** in Prometheus format on **port 8889**
* Prometheus scrapes from 8889 and **stores them for querying**

---

## 🛠️ Requirements

* Docker + Docker Compose
* Java 11+ with OpenTelemetry agent


