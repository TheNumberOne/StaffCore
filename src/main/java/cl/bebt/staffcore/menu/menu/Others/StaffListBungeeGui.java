package cl.bebt.staffcore.menu.menu.Others;

import cl.bebt.staffcore.MSGChanel.SendMsg;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.menu.PaginatedMenu;
import cl.bebt.staffcore.menu.PlayerMenuUtility;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.TpPlayers;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class StaffListBungeeGui extends PaginatedMenu{

    private final main plugin;
    private final Player p;

    public StaffListBungeeGui( PlayerMenuUtility playerMenuUtility , main plugin , Player p ){
        super( playerMenuUtility );
        this.plugin = plugin;
        this.p = p;
    }

    @Override
    public String getMenuName( ){
        return utils.chat( "&cStaffList" );
    }

    @Override
    public int getSlots( ){
        return 54;
    }

    @Override
    public void handleMenu( InventoryClickEvent e ){
        Player p = ( Player ) e.getWhoClicked( );
        ArrayList < String > players = new ArrayList <>( );
        for ( String s : main.staffMembers ) {
            if ( !players.contains( s ) ) {
                players.add( s );
            }
        }
        if ( e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING ) ) {
            p.closeInventory( );
            String target = e.getCurrentItem( ).getItemMeta( ).getPersistentDataContainer( ).get( new NamespacedKey( plugin , "name" ) , PersistentDataType.STRING );
            if ( Bukkit.getPlayer( target ) instanceof Player ) {
                TpPlayers.tpToPlayer( p , target );
            } else {
                utils.tell( p , utils.getString( "staff.staff_prefix" ) + "&7Connecting to &a" + main.playersServerMap.get( target ) + " &7where &a" + target + " &7is." );
                SendMsg.connectPlayerToServer( p.getName( ) , main.playersServerMap.get( target ) );
            }
        } else if ( e.getCurrentItem( ).getType( ).equals( Material.BARRIER ) ) {
            p.closeInventory( );
        } else if ( e.getCurrentItem( ).getType( ).equals( Material.DARK_OAK_BUTTON ) ) {
            if ( ChatColor.stripColor( e.getCurrentItem( ).getItemMeta( ).getDisplayName( ) ).equalsIgnoreCase( "Back" ) ) {
                if ( page == 0 ) {
                    p.sendMessage( ChatColor.GRAY + "You are already on the first page." );
                } else {
                    page = page - 1;
                    super.open( p );
                }
            } else if ( ChatColor.stripColor( e.getCurrentItem( ).getItemMeta( ).getDisplayName( ) ).equalsIgnoreCase( "Next" ) ) {
                if ( !((index + 1) >= players.size( )) ) {
                    page = page + 1;
                    super.open( p );
                } else {
                    p.sendMessage( ChatColor.GRAY + "You are on the last page." );
                }
            }
        }
    }

    @Override
    public void setMenuItems( ){
        addMenuBorder( );
        ArrayList < String > players = new ArrayList <>( );
        for ( String s : main.staffMembers ) {
            if ( !players.contains( s ) ) {
                players.add( s );
            }
        }
        if ( players != null && !players.isEmpty( ) ) {
            for ( int i = 0; i < getMaxItemsPerPage( ); i++ ) {
                index = getMaxItemsPerPage( ) * page + i;
                if ( index >= players.size( ) ) break;
                if ( players.get( index ) != null ) {
                    //////////////////////////////
                    ItemStack p_head = utils.getPlayerHead( players.get( index ) );
                    ItemMeta meta = p_head.getItemMeta( );
                    String ping = main.playersServerPingMap.get( players.get( index ) );
                    String server = main.playersServerMap.get( players.get( index ) );
                    String gm = main.playersServerGamemodesMap.get( players.get( index ) );
                    ArrayList < String > lore = new ArrayList <>( );
                    meta.setDisplayName( utils.chat( "&a" + players.get( index ) ) );
                    if ( utils.getBoolean( "mysql.enabled" ) ) {
                        if ( SQLGetter.isTrue( players.get( index ) , "staff" ).equals( "true" ) ) {
                            lore.add( utils.chat( "&7Staff Mode: &aTrue" ) );
                        } else {
                            lore.add( utils.chat( "&7Staff Mode: &cFalse" ) );
                        }
                        if ( SQLGetter.isTrue( players.get( index ) , "vanish" ).equals( "true" ) ) {
                            lore.add( utils.chat( "&7Vanished: &aTrue" ) );
                        } else {
                            lore.add( utils.chat( "&7Vanished: &cFalse" ) );
                        }
                        if ( SQLGetter.isTrue( players.get( index ) , "staffchat" ).equals( "true" ) ) {
                            lore.add( utils.chat( "&7Staff Chat: &aTrue" ) );
                        } else {
                            lore.add( utils.chat( "&7Staff Chat: &cFalse" ) );
                        }
                        if ( SQLGetter.isTrue( players.get( index ) , "flying" ).equals( "true" ) || GameMode.valueOf( gm ).equals( GameMode.CREATIVE ) ) {
                            lore.add( utils.chat( "&7Flying: &aTrue" ) );
                        } else {
                            lore.add( utils.chat( "&7Flying: &cFalse" ) );
                        }

                    }
                    lore.add( utils.chat( "&7Gamemode: &a" + gm ) );
                    lore.add( utils.chat( "&7Server: &a" + server ) );
                    lore.add( utils.chat( "&7Ping: &a" + ping ) );
                    meta.setLore( lore );
                    meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING , "staff" );
                    meta.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "name" ) , PersistentDataType.STRING , players.get( index ) );
                    p_head.setItemMeta( meta );
                    inventory.addItem( p_head );
                    /////////////////////////////
                }
            }
        }
    }
}