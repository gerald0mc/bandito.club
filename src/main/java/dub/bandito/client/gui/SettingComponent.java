package club.bandito.client.gui;

import club.bandito.client.module.setting.Setting;

public abstract class SettingComponent extends Component {
    private final Setting setting;

    public SettingComponent(Setting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    public Setting getSetting() {
        return setting;
    }
}
