package club.bandito.client.module.setting.customsetting;

public abstract class Action {
    private boolean value;

    public abstract void performAction();

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
