import org.springframework.web.client.RestTemplate;

public class StatsClient {
    private RestTemplate restTemplate;

    public HitResponse hit(HitRequest hitDto) {
        restTemplate.exchange();
    }

    public HitResponse stats(HitRequest hitDto) {
        restTemplate.exchange();
    }
}
