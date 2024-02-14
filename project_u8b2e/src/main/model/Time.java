package model;

// starting time and ending time of an event.
public class Time {
    private int startHour;
    private int startMin;
    private int endHour;
    private int endMin;

    // REQUIRES: valid hour (01-24) and valid minute (00-59) and
    //           endHour:endMin is later than startHour:startMin
    // EFFECTS: construct a time with starting hour and minute and ending hour and minute.
    public Time(int startHour, int startMin, int endHour, int endMin) {
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
    }

    // EFFECTS: compute the duration of a sport event in minutes.
    public int countDuration() {
        return (endHour * 60 + endMin) - (startHour * 60 + startMin);
    }

    // EFFECTS: return true if the starting time (if start = true) or the ending time (if start = false)
    //          is between the adjusted time.
    public boolean isOverlapTime(Time adjusted, boolean start) {
        int startHourAdjusted = adjusted.getStartHour();
        int startMinAdjusted = adjusted.getStartMin();
        int endHourAdjusted = adjusted.getEndHour();
        int endMinAdjusted = adjusted.getEndMin();
        int hour;
        int min;
        if (start) {
            hour = startHour;
            min = startMin;
        } else {
            hour = endHour;
            min = endMin;
        }

        return ((hour > startHourAdjusted && hour < endHourAdjusted)
                || (hour == startHourAdjusted && min >= startMinAdjusted
                    && (hour < endHourAdjusted || min <= endMinAdjusted))
                || (hour == endHourAdjusted && min <= endMinAdjusted));
    }

    // EFFECTS: return true if the object time completely overlap the adjusted time that is
    //          starting time of object < starting time of adjusted time and
    //          ending time of object > ending time of adjusted time.
    public boolean isCompletelyOverlap(Time adjusted) {
        int startHourAdjusted = adjusted.getStartHour();
        int startMinAdjusted = adjusted.getStartMin();
        int endHourAdjusted = adjusted.getEndHour();
        int endMinAdjusted = adjusted.getEndMin();

        boolean earlierStart = startHour < startHourAdjusted
                || (startHour == startHourAdjusted && startMin <= startMinAdjusted);
        boolean laterEnd = (endHour > endHourAdjusted) || (endHour == endHourAdjusted && endMin >= endMinAdjusted);
        return earlierStart && laterEnd;
    }

    // MODIFIES: this
    // EFFECTS: adjust the end time according to the duration available.
    public void adjustEndTime(int duration) {
        endHour = startHour;
        endMin = startMin;
        while (duration > 0) {
            duration--;
            endMin++;
            if (endMin == 60) {
                endMin = 0;
                endHour++;
            }
        }
    }

    // MODIFIES: this
    // EFFECT: setting startHour to the inputted value.
    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    // MODIFIES: this
    // EFFECT: setting startMin to the inputted value.
    public void setStartMin(int startMin) {
        this.startMin = startMin;
    }

    // MODIFIES: this
    // EFFECT: setting endHour to the inputted value.
    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    // MODIFIES: this
    // EFFECT: setting endMin to the inputted value.
    public void setEndMin(int endMin) {
        this.endMin = endMin;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMin() {
        return startMin;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMin() {
        return endMin;
    }
}
