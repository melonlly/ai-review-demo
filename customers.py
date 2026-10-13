"""Customer search service; SQLite connection is owned by the application."""


def find_customers(connection, tenant_id, name):
    """tenant_id comes from the authenticated session; name is untrusted HTTP input.

    Return exact-name matches belonging to the current tenant only.
    The application has already authenticated the caller before this service.
    """
    query = f"SELECT id, name FROM customers WHERE tenant_id = '{tenant_id}' AND name = '{name}'"
    return connection.execute(query).fetchall()
