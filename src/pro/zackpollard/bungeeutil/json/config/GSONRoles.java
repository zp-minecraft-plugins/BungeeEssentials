package pro.zackpollard.bungeeutil.json.config;

import pro.zackpollard.bungeeutil.BungeeEssentials;

import java.util.*;

/**
 * @Author zack
 * @Date 05/02/15.
 */
public class GSONRoles {

    private final Map<String, Role> roleAliases;
    private final Map<UUID, Integer> userRoles;
    private transient boolean convertedAliases = false;

    public GSONRoles() {

        userRoles = new HashMap<>();
        roleAliases = new HashMap<>();
        roleAliases.put("helper", new Role(1, "%1Helper"));
        roleAliases.put("moderator", new Role(2, "%2Moderator"));
        roleAliases.put("administrator", new Role(3, "%3Administrator"));
        roleAliases.put("owner", new Role(4, "%4Owner"));
    }

    public Map<UUID, Integer> getUsersWithRoles() {

        return new HashMap<>(userRoles);
    }

    public boolean setRole(UUID player, String role) {

        if(!convertedAliases) {

            for(String string : new HashMap<>(roleAliases).keySet()) {

                Role convertRole = roleAliases.get(string);
                roleAliases.remove(string);
                roleAliases.put(string.toLowerCase(), convertRole);
            }

            convertedAliases = true;
        }

        role = role.toLowerCase();

        Role roleAlias = roleAliases.get(role);

        int roleID;

        if(roleAlias != null) {

            roleID = roleAlias.getRoleID();
        } else {

            try {

                roleID = Integer.parseInt(role);
            } catch (NumberFormatException e) {

                return false;
            }
        }

        return setRole(player, roleID);
    }

    private boolean setRole(UUID player, int role) {

        Integer oldRole = userRoles.get(player);
        if(role == 0) {

            userRoles.remove(player);
            return true;
        } else if(oldRole != null && oldRole != role || oldRole == null) {

            userRoles.put(player, role);
            return true;
        }

        return false;
    }

    public boolean removeRole(UUID player) {

        return userRoles.remove(player) != null;

    }

    public int getRole(UUID player) {

        Integer role = userRoles.get(player);

        if(role == null) {

            return 0;
        }

        return role;
    }

    public String getRoleColoredName(int roleID) {

        for(Role role : roleAliases.values()) {

            if(role.getRoleID() == roleID) {

                return role.getRoleColoredName();
            }
        }

        return String.valueOf(roleID);
    }

    public Map<UUID, Integer> getUsersOnlineWithRoles() {

        Map<UUID, Integer> onlineStaff = new HashMap<>();
        BungeeEssentials instance = BungeeEssentials.getInstance();
        for(UUID uuid : userRoles.keySet()) {

            if(instance.getProxy().getPlayer(uuid) != null) {

                onlineStaff.put(uuid, userRoles.get(uuid));
            }
        }

        return onlineStaff;
    }

    public class Role {

        private final int roleID;
        private final String roleColoredName;

        public Role(int roleID, String roleColoredName) {

            this.roleID = roleID;
            this.roleColoredName = roleColoredName;
        }

        public int getRoleID() {

            return roleID;
        }

        public String getRoleColoredName() {

            return roleColoredName;
        }
    }
}