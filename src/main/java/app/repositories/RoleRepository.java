package app.repositories;

import app.entities.Role;

public interface RoleRepository {
    Role findName(String name);

}
