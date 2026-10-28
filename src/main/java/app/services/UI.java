package app.services;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import io.javelit.core.Jt;
import io.javelit.core.Server;

public class UI {

    public void runUI() {
        final var server = Server.builder(() -> appUI(), 8080).build();
        server.start();
    }

    public void appUI() {
        Jt.title("Plant - Monitoring").use();

        Jt.markdown("## A simple click app").use();

        // initialize state - values in the session state are maintained at each update
        Jt.sessionState().putIfAbsent("count", 0);

        // if button is clicked, increment the count value
        if (Jt.button("Click me").use()) {
            Jt.sessionState().computeIfPresentInt("count", (k, v) -> v + 1);
        }

        // display the count
        int count = Jt.sessionState().getInt("count");
        boolean plural = count > 1;
        Jt.markdown("The button was clicked **%s** time%s".formatted(count, plural ? "s" : ""))
                .use();

        // Use the imported library apache commons-math
        Jt.divider().use();
        Jt.markdown("## Using imported apache math dependency").use();
        double[] values = {1.2, 2.3, 3.4, 4.5, 5.6};
        DescriptiveStatistics stats = new DescriptiveStatistics(values);
        double stdDev = stats.getStandardDeviation();
        Jt.text("Computed statistic with Apache commons-math: " + stdDev).use();
    }

}
