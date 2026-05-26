import { Navbar } from "../components/Navbar";
import { Card, CardContent } from "../components/ui/card";
import { Users, Globe } from "lucide-react"; // Solo importamos lo que realmente usamos

const TEAM = [
  { name: "Alejandro Gonzalez", role: "Arquitecto de SW y Backend Developer", url: "https://github.com/alejandrogonzalezo1055", imageUrl: "/images/Gonzo.jpg" },
  { name: "Nicolas Joya", role: "Backend Developer y DB Manager", url: "https://github.com/NicoJoya", imageUrl: "/images/Nico.jpg" },
  { name: "David Mannios", role: "Gestión / Liderazgo DB", url: "https://github.com/davidr125x2", imageUrl: "/images/David.jpg" },
  { name: "Juan Sebastian Ruiz", role: "Frontend Developer", url: "https://github.com/Juanseruiz07", imageUrl: "/images/Sebas.jpg" }
];

export default function AboutUs() {
  return (
    <div className="min-h-screen bg-[#F7F7F8]">
    <Navbar />
    <div className="pt-32 pb-20 px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto">
    <div className="text-center mb-16 space-y-4">
    <h1 className="text-5xl font-black tracking-tighter text-[#0A0A0A]">Nuestro Equipo</h1>
    <p className="text-lg text-[#5F6670] max-w-2xl mx-auto">
    Detrás de Fashtoll hay un equipo apasionado por conectar la moda con la tecnología.
    </p>
    </div>

    <div className="grid grid-cols-1 s  m:grid-cols-2 lg:grid-cols-4 gap-6">
    {TEAM.map((member, index) => (
      <Card key={index} className="rounded-[32px] border-[#E5E7EB] hover:shadow-lg transition-shadow">
      <CardContent className="p-8 text-center space-y-4">
      <div className="h-24 w-24 rounded-full overflow-hidden bg-[#EEF0F3] flex items-center justify-center mx-auto shadow-sm">
      {member.imageUrl ? (
        <img
        src={member.imageUrl}
        alt={member.name}
        className="h-full w-full object-cover"
        />
      ) : (
        <span className="text-3xl font-black text-[#0A0A0A]">
        {member.name.charAt(0)}
        </span>
      )}
      </div>
      <div>
      <h3 className="font-bold text-lg text-[#0A0A0A]">{member.name}</h3>
      <p className="text-sm font-semibold text-[#5F6670]">{member.role}</p>
      </div>

      {/* Enlace Genérico */}
      <div className="pt-4">
      <a
      href={member.url}
      target="_blank"
      rel="noreferrer"
      className="inline-flex items-center gap-2 px-4 py-2 bg-gray-100 hover:bg-gray-200 rounded-full text-sm font-bold text-[#5F6670] hover:text-[#0A0A0A] transition-colors"
      >
      <Globe className="h-4 w-4" />
      Github
      </a>
      </div>
      </CardContent>
      </Card>
    ))}
    </div>
    </div>
    </div>
  );
}
