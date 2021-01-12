package cl.bebt.staffcore.commands.Staff;

import cl.bebt.staffcore.main;
import cl.bebt.staffcore.utils.OpenInvSee;
import cl.bebt.staffcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class invSeeChest implements CommandExecutor{
    
    private final main plugin;
    
    public invSeeChest( main plugin ){
        this.plugin = plugin;
        plugin.getCommand( "invsee" ).setExecutor( this );
    }
    
    
    @Override
    public boolean onCommand( CommandSender sender , Command cmd , String label , String[] args ){
        if ( !utils.isOlderVersion( ) ){
            if ( !(sender instanceof Player) ) {
                utils.tell( sender , "&aHey, you need to be a player to execute this command!" );
            } else {
                if ( args.length == 0 ) {
                    if ( sender.hasPermission( "staffcore.invsee" ) ) {
                        utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&4Wrong usage" );
                        utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + "&4Use: /invsee <playername>!" );
                    } else {
                        utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
                    }
                } else if ( args.length == 1 ) {
                    if ( Bukkit.getPlayer( args[0] ) instanceof Player ) {
                        if ( sender.hasPermission( "staffcore.invsee" ) ) {
                            Player p = ( Player ) sender;
                            Player p2 = Bukkit.getPlayer( args[0] );
                            new OpenInvSee( p , p2 );
                        } else {
                            utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "no_permissions" ) );
                        }
                    } else {
                        utils.tell( sender , plugin.getConfig( ).getString( "server_prefix" ) + plugin.getConfig( ).getString( "player_dont_exist" ) );
                    }
                }
            }
        } else {
            utils.tell(sender,plugin.getConfig( ).getString( "server_prefix" )+"&cThis command can't be executed in older versions");
        }
        return false;
    }
}
