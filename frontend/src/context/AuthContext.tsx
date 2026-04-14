import React, { useState } from "react";
import { AuthContext } from "./AuthTypes";
import type { AuthUser } from "./AuthTypes";

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(() => {
    const savedUser = localStorage.getItem("fashtoll_user");
    if (savedUser) {
      try {
        return JSON.parse(savedUser);
      } catch (e) {
        console.error("Error al restaurar la sesión:", e);
        localStorage.removeItem("fashtoll_user");
      }
    }
    return null;
  });
  const [isLoading] = useState(false);

  const login = (userData: AuthUser) => {
    setUser(userData);
    localStorage.setItem("fashtoll_user", JSON.stringify(userData));
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem("fashtoll_user");
  };

  const updateUserName = (newName: string) => {
    if (user) {
      const updatedUser = { ...user, name: newName };
      setUser(updatedUser);
      localStorage.setItem("fashtoll_user", JSON.stringify(updatedUser));
    }
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, updateUserName, isLoading }}>
      {children}
    </AuthContext.Provider>
  );
}
