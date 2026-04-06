import { BrowserRouter, Routes, Route } from "react-router-dom";
import LandingPage from "./pages/LandingPage";
import LoginSelection from "./pages/LoginSelection";
import LoginClient from "./pages/LoginClient";
import LoginBrand from "./pages/LoginBrand";
import RegisterClient from "./pages/RegisterClient";
import RegisterBrand from "./pages/RegisterBrand";
import NotFound from "./pages/NotFound";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<LoginSelection />} />
        <Route path="/login-client" element={<LoginClient />} />
        <Route path="/login-brand" element={<LoginBrand />} />
        <Route path="/register-client" element={<RegisterClient />} />
        <Route path="/register-brand" element={<RegisterBrand />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
