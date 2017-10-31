package tech.stoptalking.crop.config;


import tech.stoptalking.crop.drawable.bg.BGDrawable;
import tech.stoptalking.crop.drawable.fg.FGDrawable;
import tech.stoptalking.crop.restriction.Restriction;

/**
 * Created by tdbes on 12.09.2017.
 */

public class ViewConfig {

    //move modes
    public static final int     BG_STILL_FG_MOV_MODE = 0,
                                BG_MOV_FG_STILL_MODE = 1;

    //bound modes
    public static final int     BOUND_BG_MODE = 0,
                                BOUND_FG_MODE = 1,
                                BOUND_VIEW_MODE = 2;

    private FGDrawable  foreground;
    private BGDrawable  background;
    private Restriction restriction;
    private int         moveMode, boundMode;

    //getter

    public BGDrawable getBackground() {
        return background;
    }

    public FGDrawable getForeground() {
        return foreground;
    }

    public int getMoveMode() {
        return moveMode;
    }

    public int getBoundMode() {
        return boundMode;
    }

    public Restriction getRestriction() {
        return restriction;
    }

    //builder

    public static Builder builder() {
        return new ViewConfig().new Builder();
    }

    public class Builder {

        public Builder foreground(FGDrawable foreground) {
            ViewConfig.this.foreground = foreground;
            return this;
        }

        public Builder background (BGDrawable background) {
            ViewConfig.this.background = background;
            return this;
        }

        public Builder moveMode (int moveMode) {
            ViewConfig.this.moveMode = moveMode;
            return this;
        }

        public Builder boundMode (int boundMode) {
            ViewConfig.this.boundMode = boundMode;
            return this;
        }

        public Builder restriction (Restriction restriction) {
            ViewConfig.this.restriction = restriction;
            return this;
        }

        public ViewConfig build () {
            return ViewConfig.this;
        }
    }
}
