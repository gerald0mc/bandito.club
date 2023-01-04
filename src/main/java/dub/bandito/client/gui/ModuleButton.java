package club.bandito.client.gui;

import club.bandito.client.gui.settings.*;
import club.bandito.client.module.setting.customsetting.Action;
import club.bandito.client.module.Module;
import club.bandito.client.module.setting.*;
import club.bandito.client.module.setting.customsetting.CustomActionSetting;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleButton extends Component {

    private final Module module;
    private final List<Component> components;
    private boolean open;

    public ModuleButton(Module module, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.module = module;
        this.components = new ArrayList<>();

        final ParentSetting modParent = new ParentSetting("ModSettings");
        final Action toggleAction = new Action() {
            @Override
            public void performAction() {
                module.toggle();
                setValue(module.isEnabled());
            }
        };
        final CustomActionSetting toggleSetting = new CustomActionSetting("Toggled", toggleAction, modParent);

        components.add(new ParentComponent(modParent, 0, 0, width, height));
        components.add(new BindComponent(module, 0, 0, width, height, modParent));
        components.add(new CustomActionComponent(toggleSetting, 0, 0, width, height, modParent));

        for (Setting setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                components.add(new BooleanComponent((BooleanSetting) setting, 0, 0, width, height));
            } else if (setting instanceof PositionSetting) {
                components.add(new PositionComponent((PositionSetting) setting, 0, 0, width, height));
            } else if (setting instanceof FloatSetting) {
                components.add(new FloatComponent((FloatSetting) setting, 0, 0, width, height));
            } else if (setting instanceof ModeSetting) {
                components.add(new ModeComponent((ModeSetting) setting, 0, 0, width, height));
            } else if (setting instanceof ListSetting) {
                components.add(new ListComponent((ListSetting) setting, 0, 0, width, height));
            } else if (setting instanceof StringSetting) {
                components.add(new StringComponent((StringSetting) setting, 0, 0, width, height));
            } else if (setting instanceof ParentSetting) {
                components.add(new ParentComponent((ParentSetting) setting, 0, 0, width, height));
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (open) {
            for (Component component : components) {
                if (component instanceof SettingComponent) {
                    Setting setting = ((SettingComponent) component).getSetting();
                    if (setting.getParent() != null && !setting.getParent().getValue()) continue;
                } else if (component instanceof BindComponent) {
                    BindComponent bindComponent = (BindComponent) component;
                    if (bindComponent.parent != null && !bindComponent.parent.getValue()) continue;
                }
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }

        if (isInside(mouseX, mouseY)) {
            if (mouseButton == 0) {
                module.toggle();
            } else if (mouseButton == 1) {
                open = !open;
            }
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (open) {
            for (Component component : components) {
                if (component instanceof SettingComponent) {
                    Setting setting = ((SettingComponent) component).getSetting();
                    if (setting.getParent() != null && !setting.getParent().getValue()) continue;
                } else if (component instanceof BindComponent) {
                    BindComponent bindComponent = (BindComponent) component;
                    if (bindComponent.parent != null && !bindComponent.parent.getValue()) continue;
                }
                component.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, 0x80000000);
//        if (module.isEnabled())
//            Gui.drawRect(x + 2, y + 1, x + width - 2, y + height - 2, 0xc8573cd1);
        Gui.drawRect(x, y, x + 1, y + height, ClickGuiScreen.guiColor.getRGB());
        Gui.drawRect(x + width - 1, y, x + width, y + height, ClickGuiScreen.guiColor.getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow((module.isEnabled() ? ChatFormatting.WHITE : ChatFormatting.RESET) + trimValue("", module.getName(), open ? "-" : "+", width, 4), x + 3f, y + (height / 2f) - (9 / 2f), new Color(140, 140, 140, 255).getRGB());
        final String str = (isInside(mouseX, mouseY) ? ChatFormatting.WHITE : ChatFormatting.RESET) + (open ? "-" : "+");
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(str, x + width - 3f - (Minecraft.getMinecraft().fontRenderer.getStringWidth(str)), y + (height / 2f) - (9 / 2f), new Color(140, 140, 140, 255).getRGB());


        if (open) {
            int compY = y + height;
            for (Component component : components) {
                if (component instanceof SettingComponent) {
                    Setting setting = ((SettingComponent) component).getSetting();
                    if (setting.getParent() != null && !setting.getParent().getValue()) continue;
                } else if (component instanceof BindComponent) {
                    BindComponent bindComponent = (BindComponent) component;
                    if (bindComponent.parent != null && !bindComponent.parent.getValue()) continue;
                }
                component.x = x;
                component.y = compY;
                component.drawComponent(mouseX, mouseY, partialTicks);
                compY += component.getHeight();
            }

            Gui.drawRect(x, compY - 1, x + width, compY, ClickGuiScreen.guiColor.getRGB());
        } else {
            Gui.drawRect(x, y + height - 1, x + width, y + height, ClickGuiScreen.guiColor.getRGB());
        }
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) {
        if (open) {
            for (Component component : components) {
                if (component instanceof SettingComponent) {
                    Setting setting = ((SettingComponent) component).getSetting();
                    if (setting.getParent() != null && !setting.getParent().getValue()) continue;
                } else if (component instanceof BindComponent) {
                    BindComponent bindComponent = (BindComponent) component;
                    if (bindComponent.parent != null && !bindComponent.parent.getValue()) continue;
                }
                component.keyTyped(keyChar, keyCode);
            }
        }
    }

    @Override
    public int getHeight() {
        if(open) {
            int h = height;
            for(Component component : components) {
                if (component instanceof SettingComponent) {
                    Setting setting = ((SettingComponent) component).getSetting();
                    if (setting.getParent() != null && !setting.getParent().getValue()) continue;
                } else if (component instanceof BindComponent) {
                    BindComponent bindComponent = (BindComponent) component;
                    if (bindComponent.parent != null && !bindComponent.parent.getValue()) continue;
                }
                h += component.getHeight();
            }
            return h;
        }
        return height;
    }

    public Module getModule() {
        return module;
    }

    public List<Component> getComponents() {
        return components;
    }
}
