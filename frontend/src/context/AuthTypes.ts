import { createContext } from "react";

export interface AuthUser {
  email: string;
  name?: string;
  role: "ADMIN" | "BRAND" | "CLIENT";
  token: string;
}

export interface AuthContextType {
  user: AuthUser | null;
  login: (userData: AuthUser) => void;
  logout: () => void;
  updateUserName: (newName: string) => void;
  isLoading: boolean;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);
