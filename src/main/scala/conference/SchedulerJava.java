package conference;

import javax.management.relation.Role;
import java.util.*;

/**
 * Created by huodon on 16/1/4.
 */


public interface SchedulerJava {

    /**
     * Schedule event list as session list
     *
     * @param events event data set for select
     * @param days  group result session count
     * @param duration session total duration time
     * @param durationFactor the result must be between duration and duration+durationFactor
     * @return  List<List<Event>>
     */
    default List<List<Event>> schedule(List<Event> events, int days, int duration, int durationFactor) {
        final List<List<Event>> matched = new LinkedList<>();

        final List<Event> sel = new LinkedList<>();

        int sum = 0;
        int count = 0;

        final Set<Event> visited = new HashSet<>();

        for (int i = 0; i < events.size(); i++) {
            if (visited.contains(events.get(i))) continue;

            sel.add(events.get(i));
            sum = events.get(i).duration();

            if (sum == duration) {
                LinkedList<Event> temp = new LinkedList<>();
                temp.add(events.get(i));
                matched.add(temp);
            }

            if (matched.size() == days) return matched;

            if (sum < duration) {
                for (int j = i + 1; j < events.size(); j++) {
                    sum += events.get(j).duration();
                    sel.add(events.get(j));

                    boolean cond;
                    if (durationFactor == 0) cond = sum == duration;
                    else cond = sum >= duration && sum <= duration + durationFactor;

                    if (cond) {
                        visited.addAll(sel);
                        List<Event> temp = new LinkedList<>();
                        temp.addAll(sel);
                        matched.add(temp);
                        count += 1;
                        sel.clear();
                        if (count == days) return matched;
                        break;
                    }

                    if (sum > duration) {
                        sum -= events.get(j).duration();
                        sel.remove(events.get(j));
                    }
                    if (j == (events.size() - 1)) {
                        sel.clear();
                        sum = 0;
                    }
                }
            }
        }

        return matched;
    }
}
