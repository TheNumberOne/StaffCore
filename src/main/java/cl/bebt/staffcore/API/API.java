package cl.bebt.staffcore.API;

import cl.bebt.staffcore.commands.Staff.CheckAlts;
import cl.bebt.staffcore.main;
import cl.bebt.staffcore.sql.SQLGetter;
import cl.bebt.staffcore.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"unused"})
public class API {
    private static main plugin;
    
    public API( main plugin ){
        API.plugin = plugin;
    }
    
    public static String getIP( String player ){
        if ( utils.mysqlEnabled( ) )
            return SQLGetter.getIp( player );
        try {
            return Bukkit.getPlayer( player ).getAddress( ).getAddress( ).toString( );
        } catch ( NullPointerException error ) {
            return "";
        }
    }
    
    public static int getPing( String player ){
        try {
            return utils.getPing( Bukkit.getPlayer( player ) );
        } catch ( NullPointerException error ) {
            return 0;
        }
    }
    
    public static ArrayList < String > getSavedPlayerList( ){
        return utils.getUsers( );
    }
    
    public static List < String > getPlayerAlts( String player ){
        return CheckAlts.alts( player );
    }
    
    public static void clearPlayerChat( Player player ){
        utils.ccPlayer( player );
    }
    
    public static void clearAllPlayersChat( ){
        utils.ccAll( );
    }
    
    public static boolean isRegistered( String player ){
        return utils.isRegistered( player );
    }
    
    public static boolean mysqlEnabled( ){
        return utils.mysqlEnabled( );
    }
    
    public static boolean getFrozenStatus( String player ){
        if ( mysqlEnabled( ) )
            return SQLGetter.isTrue( player , "frozen" ).equalsIgnoreCase( "true" );
        try {
            return Bukkit.getPlayer( player ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "frozen" ) , PersistentDataType.STRING );
        } catch ( NullPointerException error ) {
            return false;
        }
    }
    
    public static boolean getVanishedStatus( String player ){
        if ( mysqlEnabled( ) )
            return SQLGetter.isTrue( player , "vanish" ).equalsIgnoreCase( "true" );
        try {
            return Bukkit.getPlayer( player ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "vanished" ) , PersistentDataType.STRING );
        } catch ( NullPointerException error ) {
            return false;
        }
    }
    
    public static boolean getStaffStatus( String player ){
        if ( mysqlEnabled( ) )
            return SQLGetter.isTrue( player , "staff" ).equalsIgnoreCase( "true" );
        try {
            return Bukkit.getPlayer( player ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staff" ) , PersistentDataType.STRING );
        } catch ( NullPointerException error ) {
            return false;
        }
    }
    
    public static boolean getStaffChatStatus( String player ){
        if ( mysqlEnabled( ) )
            return SQLGetter.isTrue( player , "staffchat" ).equalsIgnoreCase( "true" );
        try {
            return Bukkit.getPlayer( player ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "staffchat" ) , PersistentDataType.STRING );
        } catch ( NullPointerException error ) {
            return false;
        }
    }
    
    public static boolean getFlyingStatus( String player ){
        if ( mysqlEnabled( ) )
            return SQLGetter.isTrue( player , "flying" ).equalsIgnoreCase( "true" );
        try {
            return Bukkit.getPlayer( player ).getPersistentDataContainer( ).has( new NamespacedKey( plugin , "flying" ) , PersistentDataType.STRING );
        } catch ( NullPointerException error ) {
            return false;
        }
    }
    
    public static boolean isBanned( String player ){
        if ( mysqlEnabled( ) )
            return SQLGetter.getBannedPlayers( ).contains( player );
        List < String > bannedPlayers = new ArrayList <>( );
        ConfigurationSection bans = plugin.bans.getConfig( ).getConfigurationSection( "bans" );
        for ( String s : bans.getKeys( false ) )
            bannedPlayers.add( plugin.bans.getConfig( ).getString( "bans." + s + ".name" ) );
        return bannedPlayers.contains( player );
    }
    
    public static boolean isWarned( String player ){
        if ( mysqlEnabled( ) )
            return SQLGetter.getWarnedPlayers( ).contains( player );
        List < String > warnedPlayers = new ArrayList <>( );
        ConfigurationSection warns = plugin.warns.getConfig( ).getConfigurationSection( "warns" );
        for ( String s : warns.getKeys( false ) )
            warnedPlayers.add( plugin.warns.getConfig( ).getString( "warns." + s + ".name" ) );
        return warnedPlayers.contains( player );
    }
    
    public static void setFrozenStatus( Player target , String sender , boolean status ){
        FreezePlayer.FreezePlayer( target , sender , status );
    }
    
    public static void setVanishStatus( Player target , boolean status ){
        SetVanish.setVanish( target , status );
    }
    
    public static void setStaffStatus( Player target , boolean status ){
        if ( status ) {
            SetStaffItems.On( target );
        } else {
            SetStaffItems.Off( target );
        }
    }
    
    public static void setStaffChatStatus( Player target , boolean status ){
        if ( mysqlEnabled( ) ) {
            if ( status ) {
                SQLGetter.setTrue( target.getName( ) , "staffchat" , "true" );
            } else {
                SQLGetter.setTrue( target.getName( ) , "staffchat" , "false" );
            }
        } else if ( status ) {
            target.getPersistentDataContainer( ).set( new NamespacedKey( plugin , "staffchat" ) , PersistentDataType.STRING , "staffchat" );
        } else {
            target.getPersistentDataContainer( ).remove( new NamespacedKey( plugin , "staffchat" ) );
        }
    }
    
    public static void setFlyingStatus( Player target , boolean status ){
        SetFly.SetFly( target , status );
    }
    
    public static ArrayList < String > getBannedPlayers( ){
        ArrayList < String > bannedPlayers = new ArrayList <>( );
        if ( utils.mysqlEnabled( ) ) {
            bannedPlayers.addAll( SQLGetter.getBannedPlayers( ) );
        } else {
            ConfigurationSection inventorySection = plugin.bans.getConfig( ).getConfigurationSection( "bans" );
            for ( String key : inventorySection.getKeys( false ) ) {
                String name = plugin.bans.getConfig( ).getString( "bans." + key + ".name" );
                if ( !bannedPlayers.contains( name ) )
                    bannedPlayers.add( name );
            }
        }
        return bannedPlayers;
    }
    
    public static ArrayList < String > getWarnedPlayers( ){
        return utils.getWarnedPlayers( );
    }
    
    public static Boolean isStillBaned( int Id ){
        if ( utils.mysqlEnabled( ) )
            try {
                Date now = new Date( );
                Date exp_date = (new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" )).parse( SQLGetter.getBaned( Id , "ExpDate" ) );
                if ( now.after( exp_date ) ) {
                    SQLGetter.setBan( Id , "closed" );
                    return false;
                }
                if ( !SQLGetter.BansTableExists( ) ) {
                    SQLGetter.createBansTable( );
                    return false;
                }
                return Boolean.TRUE;
            } catch ( ParseException | NullPointerException ignored ) {
                return false;
            }
        try {
            Date now = new Date( );
            Date exp_date = (new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" )).parse( plugin.bans.getConfig( ).getString( "bans." + Id + ".expdate" ) );
            if ( now.after( exp_date ) ) {
                plugin.bans.getConfig( ).set( "bans." + Id + ".status" , "closed" );
                plugin.bans.saveConfig( );
                plugin.bans.reloadConfig( );
                return false;
            }
            return true;
        } catch ( ParseException | NullPointerException ignored ) {
            plugin.bans.getConfig( ).set( "bans." + Id + ".status" , "closed" );
            plugin.bans.saveConfig( );
            plugin.bans.reloadConfig( );
            return false;
        }
    }
    
    public static Boolean isStillWarned( int Id ){
        if ( utils.mysqlEnabled( ) )
            try {
                Date now = new Date( );
                Date exp_date = (new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" )).parse( SQLGetter.getWarned( Id , "ExpDate" ) );
                if ( now.after( exp_date ) ) {
                    SQLGetter.setWarn( Id , "closed" );
                    return false;
                }
                if ( !SQLGetter.WarnsTableExists( ) ) {
                    SQLGetter.createWarnsTable( );
                    return false;
                }
                return true;
            } catch ( ParseException | NullPointerException ignored ) {
                return false;
            }
        try {
            Date now = new Date( );
            Date exp_date = (new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" )).parse( plugin.warns.getConfig( ).getString( "warns." + Id + ".expdate" ) );
            if ( now.after( exp_date ) ) {
                plugin.warns.getConfig( ).set( "warns." + Id + ".status" , "closed" );
                plugin.warns.saveConfig( );
                plugin.warns.reloadConfig( );
                return false;
            }
            return true;
        } catch ( ParseException | NullPointerException ignored ) {
            plugin.warns.getConfig( ).set( "warns." + Id + ".status" , "closed" );
            plugin.warns.saveConfig( );
            plugin.warns.reloadConfig( );
            return false;
        }
    }
    
    public static void banPlayer( CommandSender sender , String banned , String reason , Long amount , String time ){
        BanPlayer.BanCooldown( sender , banned , reason , amount , time );
    }
    
    public static void banPlayer( CommandSender sender , String banned , String reason ){
        BanPlayer.BanPlayer( sender , banned , reason );
    }
    
    public static void warnPlayer( Player player , String warned , String reason , Long amount , String time ){
        WarnPlayer.createWarn( player , warned , reason , amount , time );
    }
    
    public static void warnPlayer( Player player , String warned , String reason ){
        WarnPlayer.createWarn( player , warned , reason , utils.getInt( "warns.expire_after" ) , utils.getString( "warns.expire_after_quantity" ) );
    }
}
