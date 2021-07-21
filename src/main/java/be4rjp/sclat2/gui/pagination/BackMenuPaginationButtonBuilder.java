package be4rjp.sclat2.gui.pagination;

import be4rjp.sclat2.Sclat;
import be4rjp.sclat2.gui.MainMenuGUI;
import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.language.MessageManager;
import be4rjp.sclat2.player.SclatPlayer;
import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.pagination.SGPaginationButtonBuilder;
import com.samjakob.spigui.pagination.SGPaginationButtonType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class BackMenuPaginationButtonBuilder implements SGPaginationButtonBuilder {
    
    private static Map<Lang, SGPaginationButtonBuilder> buttonBuilderMap = new HashMap<>();
    
    public static SGPaginationButtonBuilder getPaginationButtonBuilder(Lang lang){
        return buttonBuilderMap.computeIfAbsent(lang, k -> new BackMenuPaginationButtonBuilder(lang));
    }
    
    
    private final Lang lang;
    
    public BackMenuPaginationButtonBuilder(Lang lang){
        this.lang = lang;
    }
    
    @Override
    public SGButton buildPaginationButton(SGPaginationButtonType type, SGMenu inventory) {
        switch (type) {
            case PREV_BUTTON:
                if (inventory.getCurrentPage() > 0) return new SGButton(new ItemBuilder(Material.ARROW)
                        .name("&a&l\u2190 " + MessageManager.getText(lang, "gui-page-back"))
                        .lore(String.format(MessageManager.getText(lang, "gui-page-back-to-click"), inventory.getCurrentPage()))
                        .build()
                ).withListener(event -> {
                    event.setCancelled(true);
                    inventory.previousPage(event.getWhoClicked());
                });
                else return null;
            
            case CURRENT_BUTTON:
                return new SGButton(new ItemBuilder(Material.OAK_DOOR)
                        .name(MessageManager.getText(lang, "gui-back-to-menu")).build()
                ).withListener(event -> {
                    if(!(event.getWhoClicked() instanceof Player)) return;
                    Player player = (Player) event.getWhoClicked();
    
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            SclatPlayer sclatPlayer = SclatPlayer.getSclatPlayer(player);
                            MainMenuGUI.openMainMenu(sclatPlayer);
                        }
                    }.runTaskAsynchronously(Sclat.getPlugin());
                });
            
            case NEXT_BUTTON:
                if (inventory.getCurrentPage() < inventory.getMaxPage() - 1) return new SGButton(new ItemBuilder(Material.ARROW)
                        .name("&a&l" + MessageManager.getText(lang, "gui-page-next") + " \u2192")
                        .lore(String.format(MessageManager.getText(lang, "gui-page-next-to-click"), (inventory.getCurrentPage() + 2))
                        ).build()
                ).withListener(event -> {
                    event.setCancelled(true);
                    inventory.nextPage(event.getWhoClicked());
                });
                else return null;
            
            case UNASSIGNED:
            default:
                return null;
        }
    }
}

