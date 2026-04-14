import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { Navbar } from "../components/Navbar";
import { Card, CardContent } from "../components/ui/card";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";

import { useAuth } from "../hooks/useAuth";
import { 
  getBrandProfile, 
  getMyProducts, 
  updateBrandProfile, 
  changeBrandPassword,
  createProduct,
  updateProduct,
  deleteProduct
} from "../services/brandService";
import type { BrandProfile, ProductManagement, UpdateBrandData, PasswordData } from "../services/brandService";
import { getProductTypes, getTags } from "../services/productService";
import type { ProductType, Tag } from "../services/productService";
import { 
  Building2, 
  Package, 
  Plus, 
  Users, 
  Star, 
  ExternalLink,
  Loader2,
  Edit,
  Trash2,
  X,
  ChevronLeft,
  ChevronRight,
  Check,
  Eye
} from "lucide-react";
import { VerifiedBadge } from "../components/ui/VerifiedBadge";
import { useNavigate } from "react-router-dom";
import React, { useState } from "react";

export default function BrandDashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  // Modals state
  const [isEditProfileOpen, setIsEditProfileOpen] = useState(false);
  const [isProductModalOpen, setIsProductModalOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState<ProductManagement | null>(null);

  // Forms state
  const [profileForm, setProfileForm] = useState<UpdateBrandData>({
    name: "",
    pictureUrl: "",
    linkOfficial: ""
  });
  const [passwordForm, setPasswordForm] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: ""
  });
  
  interface ProductFormValues extends Partial<ProductManagement> {
    productTypeId: string;
    tagIds: number[];
  }

  const [productForm, setProductForm] = useState<ProductFormValues>({
    name: "",
    description: "",
    price: 0,
    productTypeId: "",
    generalFit: "REGULAR",
    gender: "UNISEX",
    color: "BLACK",
    available: true,
    linkProduct: "",
    imageUrls: [""],
    tagIds: []
  });

  // Queries
  const { data: profile, isLoading: isProfileLoading } = useQuery<BrandProfile>({
    queryKey: ["brandProfile"],
    queryFn: () => getBrandProfile(user?.token || ""),
    enabled: !!user?.token,
  });

  const { data: products, isLoading: isProductsLoading } = useQuery<ProductManagement[]>({
    queryKey: ["brandProducts"],
    queryFn: () => getMyProducts(user?.token || ""),
    enabled: !!user?.token,
  });

  const { data: productTypes } = useQuery<ProductType[]>({
    queryKey: ["productTypes"],
    queryFn: () => getProductTypes(user?.token || ""),
    enabled: !!user?.token,
  });

  const { data: tags } = useQuery<Tag[]>({
    queryKey: ["tags"],
    queryFn: () => getTags(user?.token || ""),
    enabled: !!user?.token,
  });

  // Mutations
  const updateProfileMutation = useMutation({
    mutationFn: (data: UpdateBrandData) => updateBrandProfile(user?.token || "", data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["brandProfile"] });
      alert("Perfil actualizado con éxito");
      setIsEditProfileOpen(false);
    },
    onError: () => {
      alert("Error al actualizar el perfil");
      setIsEditProfileOpen(false);
    }
  });

  const changePasswordMutation = useMutation({
    mutationFn: (data: PasswordData) => changeBrandPassword(user?.token || "", data),
    onSuccess: () => {
      alert("Contraseña cambiada con éxito");
      setPasswordForm({ currentPassword: "", newPassword: "", confirmPassword: "" });
    },
    onError: () => {
      alert("Error al cambiar la contraseña. Verifica tu contraseña actual.");
    }
  });

  const createProductMutation = useMutation({
    mutationFn: (data: Partial<ProductManagement>) => createProduct(user?.token || "", data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["brandProducts"] });
      alert("Producto creado con éxito");
      setIsProductModalOpen(false);
    }
  });

  const updateProductMutation = useMutation({
    mutationFn: ({ id, data }: { id: number, data: Partial<ProductManagement> }) => updateProduct(user?.token || "", id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["brandProducts"] });
      alert("Producto actualizado con éxito");
      setIsProductModalOpen(false);
    }
  });

  const deleteProductMutation = useMutation({
    mutationFn: (id: number) => deleteProduct(user?.token || "", id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["brandProducts"] });
      alert("Producto eliminado");
    }
  });

  // Sync form with profile data using "set during render" pattern
  const [prevProfileData, setPrevProfileData] = useState<{name: string, pic: string, link: string} | null>(null);
  
  if (profile && (!prevProfileData || 
      prevProfileData.name !== profile.name || 
      prevProfileData.pic !== (profile.pictureUrl || "") || 
      prevProfileData.link !== (profile.linkOfficial || ""))) {
    setPrevProfileData({
      name: profile.name,
      pic: profile.pictureUrl || "",
      link: profile.linkOfficial || ""
    });
    setProfileForm({
      name: profile.name,
      pictureUrl: profile.pictureUrl || "",
      linkOfficial: profile.linkOfficial || ""
    });
  }

  const [prevEditingProduct, setPrevEditingProduct] = useState<ProductManagement | null>(null);
  if (editingProduct !== prevEditingProduct) {
    setPrevEditingProduct(editingProduct);
    if (editingProduct) {
      setProductForm({
        name: editingProduct.name,
        description: editingProduct.description,
        price: editingProduct.price,
        productTypeId: editingProduct.productType.id.toString(),
        generalFit: editingProduct.generalFit,
        gender: editingProduct.gender,
        color: editingProduct.color,
        available: editingProduct.available,
        linkProduct: editingProduct.linkProduct || "",
        imageUrls: editingProduct.imageUrls.length > 0 ? [...editingProduct.imageUrls, ""] : [""],
        tagIds: Array.isArray(editingProduct.tags) ? editingProduct.tags.map((t) => t.id) : []
      });
    } else {
      setProductForm({
        name: "",
        description: "",
        price: 0,
        productTypeId: "",
        generalFit: "REGULAR",
        gender: "UNISEX",
        color: "BLACK",
        available: true,
        linkProduct: "",
        imageUrls: [""],
        tagIds: []
      });
    }
  }

  const handleLogout = () => {
    if (window.confirm("¿Estás seguro de que quieres cerrar sesión?")) {
      logout();
      navigate("/");
    }
  };

  const handleDeleteProduct = (id: number) => {
    if (window.confirm("¿Estás seguro de que quieres eliminar el producto? No se podrá recuperar cuando lo hagas")) {
      deleteProductMutation.mutate(id);
    }
  };

  const handleUpdateProfile = (e: React.FormEvent) => {
    e.preventDefault();
    updateProfileMutation.mutate(profileForm);
  };

  const handleChangePassword = (e: React.FormEvent) => {
    e.preventDefault();
    if (passwordForm.newPassword !== passwordForm.confirmPassword) {
      alert("Las contraseñas no coinciden");
      return;
    }
    changePasswordMutation.mutate({
      currentPassword: passwordForm.currentPassword,
      newPassword: passwordForm.newPassword
    });
  };

  const handleSaveProduct = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!productForm.productTypeId) {
      alert("Por favor selecciona un tipo de producto");
      return;
    }

    const finalImageUrls = (productForm.imageUrls || []).filter((url: string) => url.trim() !== "");
    const finalData = {
      ...productForm,
      productTypeId: parseInt(productForm.productTypeId),
      imageUrls: finalImageUrls
    };

    if (editingProduct) {
      updateProductMutation.mutate({ id: editingProduct.id, data: finalData as any });
    } else {
      createProductMutation.mutate(finalData as any);
    }
  };

  const handleImageUrlChange = (index: number, value: string) => {
    const newUrls = [...(productForm.imageUrls || [])];
    newUrls[index] = value;
    
    // Add new empty field if last one is filled
    if (index === newUrls.length - 1 && value !== "") {
      newUrls.push("");
    }
    
    setProductForm({ ...productForm, imageUrls: newUrls });
  };

  const toggleTag = (tagId: number) => {
    const newTags = (productForm.tagIds || []).includes(tagId)
      ? (productForm.tagIds || []).filter((id: number) => id !== tagId)
      : [...(productForm.tagIds || []), tagId];
    setProductForm({ ...productForm, tagIds: newTags });
  };

  const translateEnum = (value: string) => {
    const translations: Record<string, string> = {
      // GeneralFit
      "SLIM": "Slim",
      "REGULAR": "Regular",
      "LOOSE": "Holgado",
      // Gender
      "MALE": "Masculino",
      "FEMALE": "Femenino",
      "UNISEX": "Unisex",
      // Color
      "WHITE": "Blanco", "BLACK": "Negro", "GREY": "Gris", "BROWN": "Café", 
      "BEIGE": "Beige", "GREEN": "Verde", "BLUE": "Azul", "PURPLE": "Morado", 
      "RED": "Rojo", "ORANGE": "Naranja", "PINK": "Rosado", "YELLOW": "Amarillo", 
      "GOLD": "Dorado", "SILVER": "Plateado", "MULTICOLOR": "Multicolor", "OTHER": "Otro",
      // TagType
      "STYLE": "Estilo", "FIT": "Fit", "OCCASION": "Ocasión",
      // Category
      "TOPS": "Tops", "BOTTOMS": "Bottoms", "OUTERWEAR": "Outerwear", "FULL_BODY": "Full Body"
    };
    return translations[value] || value;
  };

  if (isProfileLoading || isProductsLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-[#F7F7F8]">
        <Loader2 className="h-10 w-10 animate-spin text-[#0A0A0A]" />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-[#F7F7F8]">
      <Navbar />

      <div className="pt-32 pb-20 px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto space-y-8">
          
          {/* Header de la Marca */}
          <section className="relative">
            <Card className="rounded-[40px] border-[#E5E7EB] overflow-hidden shadow-xl bg-white">
              <div className="h-32 bg-gradient-to-r from-[#0A0A0A] to-[#2B2B2B]" />
              <CardContent className="px-10 pb-10">
                <div className="flex flex-col md:flex-row items-end gap-6 -mt-16">
                  <div className="relative">
                    <div className="h-32 w-32 rounded-[32px] bg-white border-4 border-white shadow-lg flex items-center justify-center overflow-hidden">
                      {profile?.pictureUrl ? (
                        <img src={profile.pictureUrl} alt={profile.name} className="h-full w-full object-cover" />
                      ) : (
                        <Building2 className="h-12 w-12 text-[#9CA3AF]" />
                      )}
                    </div>
                    {profile?.isVerified && (
                      <VerifiedBadge size="lg" className="absolute -bottom-2 -right-2 border-4 border-white rounded-full bg-white shadow-lg" />
                    )}
                  </div>
                  
                  <div className="flex-1 space-y-2 text-center md:text-left">
                    <div className="flex flex-col md:flex-row md:items-center gap-2 md:gap-2">
                       <div className="flex items-center gap-2 justify-center md:justify-start">
                         <h1 className="text-4xl font-black tracking-tight text-[#0A0A0A]">{profile?.name}</h1>
                         {profile?.isVerified && <VerifiedBadge size="md" />}
                       </div>
                    </div>
                    <div className="flex flex-wrap justify-center md:justify-start gap-4">
                      {profile?.linkOfficial && (
                        <a 
                          href={profile.linkOfficial} 
                          target="_blank" 
                          rel="noopener noreferrer"
                          className="flex items-center gap-1 text-sm font-bold text-[#5F6670] hover:text-[#0A0A0A] transition-colors"
                        >
                          <ExternalLink className="h-4 w-4" />
                          Sitio Oficial
                        </a>
                      )}
                    </div>
                  </div>

                  <div className="flex gap-2">
                    <Button 
                      variant="outline" 
                      className="rounded-xl font-bold"
                      onClick={() => setIsEditProfileOpen(true)}
                    >
                      Editar Perfil
                    </Button>
                    <Button onClick={handleLogout} variant="ghost" className="text-red-500 hover:text-red-600 font-bold">Salir</Button>
                  </div>
                </div>

                {/* Stats */}
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mt-10 pt-10 border-t border-[#E5E7EB]">
                  <div className="text-center md:text-left">
                    <div className="flex items-center justify-center md:justify-start gap-2 text-[#5F6670] mb-1">
                      <Users className="h-4 w-4" />
                      <span className="text-xs font-black uppercase tracking-widest">Seguidores</span>
                    </div>
                    <p className="text-2xl font-black text-[#0A0A0A]">{profile?.followers || 0}</p>
                  </div>
                  <div className="text-center md:text-left">
                    <div className="flex items-center justify-center md:justify-start gap-2 text-[#5F6670] mb-1">
                      <Star className="h-4 w-4" />
                      <span className="text-xs font-black uppercase tracking-widest">Calificación</span>
                    </div>
                    <p className="text-2xl font-black text-[#0A0A0A]">{profile?.rating ? profile.rating.toFixed(1) : "0.0"}</p>
                  </div>
                </div>
              </CardContent>
            </Card>
          </section>

          {/* Gestión de Productos */}
          <section className="space-y-6">
            <div className="flex items-center justify-between">
              <div>
                <h2 className="text-2xl font-black tracking-tight text-[#0A0A0A]">Mis Productos</h2>
                <p className="text-[#5F6670] font-semibold text-sm">Gestiona el inventario de tu marca</p>
              </div>
              <Button 
                className="rounded-2xl h-12 px-6 flex items-center gap-2"
                onClick={() => { setEditingProduct(null); setIsProductModalOpen(true); }}
              >
                <Plus className="h-5 w-5" />
                Agregar Producto
              </Button>
            </div>

            {products && products.length > 0 ? (
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                {products.map((p) => (
                  <ProductCard key={p.id} product={p} onEdit={() => { setEditingProduct(p); setIsProductModalOpen(true); }} onDelete={() => handleDeleteProduct(p.id)} translateEnum={translateEnum} />
                ))}
              </div>
            ) : (
              <Card className="rounded-[40px] border-dashed border-2 py-20 text-center">
                <Package className="h-12 w-12 text-[#9CA3AF] mx-auto mb-4" />
                <h3 className="text-xl font-bold">Aún no tienes productos registrados</h3>
                <p className="text-[#5F6670] mb-8">Empieza publicando tu primer diseño en Fashtoll.</p>
                <Button className="rounded-2xl" onClick={() => { setEditingProduct(null); setIsProductModalOpen(true); }}>Crear mi primer producto</Button>
              </Card>
            )}
          </section>

        </div>
      </div>

      {/* Modal Editar Perfil */}
      {isEditProfileOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/60 backdrop-blur-sm" onClick={() => setIsEditProfileOpen(false)} />
          <div className="relative bg-white rounded-[32px] w-full max-w-2xl max-h-[90vh] overflow-y-auto shadow-2xl">
            <div className="p-8 space-y-8">
              <div className="flex justify-between items-center">
                <h2 className="text-3xl font-black">Editar Perfil</h2>
                <button onClick={() => setIsEditProfileOpen(false)} className="p-2 hover:bg-gray-100 rounded-full transition-colors">
                  <X className="h-6 w-6" />
                </button>
              </div>

              <form onSubmit={handleUpdateProfile} className="space-y-6">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div className="space-y-4">
                    <div className="flex flex-col items-center gap-4 p-6 bg-gray-50 rounded-3xl border border-dashed border-gray-200">
                      <div className="h-32 w-32 rounded-3xl overflow-hidden bg-white shadow-md">
                        {profileForm.pictureUrl ? (
                          <img src={profileForm.pictureUrl} alt="Preview" className="h-full w-full object-cover" />
                        ) : (
                          <Building2 className="h-12 w-12 text-gray-300 m-auto mt-10" />
                        )}
                      </div>
                      <p className="text-xs font-bold text-gray-500 uppercase tracking-widest">Vista previa</p>
                    </div>
                  </div>
                  
                  <div className="space-y-4">
                    <div className="space-y-2">
                      <Label htmlFor="name" className="font-bold">Nombre de la Marca</Label>
                      <Input
                        id="name"
                        value={profileForm.name}
                        onChange={(e) => setProfileForm({ ...profileForm, name: e.target.value })}
                        className="rounded-xl border-gray-200 h-12"
                      />
                    </div>
                    <div className="space-y-2">
                      <Label htmlFor="email" className="font-bold text-gray-400">Email (No editable)</Label>
                      <Input
                        id="email"
                        value={profile?.email}
                        disabled
                        className="rounded-xl bg-gray-50 border-gray-200 h-12 text-gray-400 cursor-not-allowed"
                      />
                    </div>
                  </div>
                </div>

                <div className="space-y-4">
                  <div className="space-y-2">
                    <Label htmlFor="pictureUrl" className="font-bold">Picture URL</Label>
                    <Input
                      id="pictureUrl"
                      placeholder="https://ejemplo.com/imagen.jpg"
                      value={profileForm.pictureUrl}
                      onChange={(e) => setProfileForm({ ...profileForm, pictureUrl: e.target.value })}
                      className="rounded-xl border-gray-200 h-12"
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="linkOfficial" className="font-bold">Link Oficial</Label>
                    <Input
                      id="linkOfficial"
                      placeholder="https://tumarca.com"
                      value={profileForm.linkOfficial}
                      onChange={(e) => setProfileForm({ ...profileForm, linkOfficial: e.target.value })}
                      className="rounded-xl border-gray-200 h-12"
                    />
                  </div>
                </div>

                <Button 
                  type="submit" 
                  className="w-full h-14 rounded-2xl font-black text-lg bg-[#0A0A0A] hover:bg-[#2B2B2B]"
                  disabled={updateProfileMutation.isPending}
                >
                  {updateProfileMutation.isPending ? <Loader2 className="animate-spin" /> : "Guardar Cambios"}
                </Button>
              </form>

              <div className="pt-8 border-t border-gray-100">
                <h3 className="text-xl font-bold mb-6">Cambiar Contraseña</h3>
                <form onSubmit={handleChangePassword} className="space-y-4">
                  <div className="space-y-2">
                    <Label htmlFor="currentPass" className="font-bold">Contraseña Actual</Label>
                    <Input
                      id="currentPass"
                      type="password"
                      value={passwordForm.currentPassword}
                      onChange={(e) => setPasswordForm({ ...passwordForm, currentPassword: e.target.value })}
                      className="rounded-xl border-gray-200 h-12"
                    />
                  </div>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div className="space-y-2">
                      <Label htmlFor="newPass" className="font-bold">Nueva Contraseña</Label>
                      <Input
                        id="newPass"
                        type="password"
                        value={passwordForm.newPassword}
                        onChange={(e) => setPasswordForm({ ...passwordForm, newPassword: e.target.value })}
                        className="rounded-xl border-gray-200 h-12"
                      />
                    </div>
                    <div className="space-y-2">
                      <Label htmlFor="confirmPass" className="font-bold">Confirmar Nueva Contraseña</Label>
                      <Input
                        id="confirmPass"
                        type="password"
                        value={passwordForm.confirmPassword}
                        onChange={(e) => setPasswordForm({ ...passwordForm, confirmPassword: e.target.value })}
                        className="rounded-xl border-gray-200 h-12"
                      />
                    </div>
                  </div>
                  <Button 
                    type="submit" 
                    variant="outline"
                    className="w-full h-12 rounded-xl font-bold"
                    disabled={changePasswordMutation.isPending}
                  >
                    Actualizar Contraseña
                  </Button>
                </form>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Modal Producto */}
      {isProductModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/60 backdrop-blur-sm" onClick={() => setIsProductModalOpen(false)} />
          <div className="relative bg-white rounded-[32px] w-full max-w-4xl max-h-[90vh] overflow-y-auto shadow-2xl">
            <div className="p-8 space-y-8">
              <div className="flex justify-between items-center">
                <h2 className="text-3xl font-black">{editingProduct ? "Editar Producto" : "Agregar Nuevo Producto"}</h2>
                <button onClick={() => setIsProductModalOpen(false)} className="p-2 hover:bg-gray-100 rounded-full transition-colors">
                  <X className="h-6 w-6" />
                </button>
              </div>

              <form onSubmit={handleSaveProduct} className="space-y-8">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                  {/* Columna Izquierda */}
                  <div className="space-y-6">
                    <div className="space-y-2">
                      <Label htmlFor="prodName" className="font-bold">Nombre del Producto</Label>
                      <Input
                        id="prodName"
                        value={productForm.name}
                        onChange={(e) => setProductForm({ ...productForm, name: e.target.value })}
                        className="rounded-xl border-gray-200 h-12"
                        required
                      />
                    </div>

                    <div className="space-y-2">
                      <Label htmlFor="prodDesc" className="font-bold">Descripción</Label>
                      <textarea
                        id="prodDesc"
                        value={productForm.description}
                        onChange={(e) => setProductForm({ ...productForm, description: e.target.value })}
                        className="w-full rounded-xl border border-gray-200 p-3 h-32 focus:outline-none focus:ring-2 focus:ring-black/5"
                        required
                      />
                    </div>

                    <div className="grid grid-cols-2 gap-4">
                      <div className="space-y-2">
                        <Label htmlFor="prodPrice" className="font-bold">Precio ($)</Label>
                        <div className="relative">
                          <span className="absolute left-4 top-1/2 -translate-y-1/2 font-bold">$</span>
                          <Input
                            id="prodPrice"
                            type="number"
                            value={productForm.price}
                            onChange={(e) => setProductForm({ ...productForm, price: parseFloat(e.target.value) })}
                            className="rounded-xl border-gray-200 h-12 pl-8"
                            required
                          />
                        </div>
                      </div>
                      <div className="space-y-2">
                        <Label className="font-bold">Disponibilidad</Label>
                        <div className="flex items-center gap-2 h-12">
                          <button
                            type="button"
                            onClick={() => setProductForm({ ...productForm, available: !productForm.available })}
                            className={`flex-1 h-full rounded-xl font-bold transition-all ${
                              productForm.available ? "bg-green-100 text-green-700 border-2 border-green-200" : "bg-red-100 text-red-700 border-2 border-red-200"
                            }`}
                          >
                            {productForm.available ? "Disponible" : "Agotado"}
                          </button>
                        </div>
                      </div>
                    </div>

                    <div className="grid grid-cols-1 gap-4">
                      <div className="space-y-2">
                        <Label className="font-bold">Tipo de Producto</Label>
                        <select
                          value={productForm.productTypeId}
                          onChange={(e) => setProductForm({ ...productForm, productTypeId: e.target.value })}
                          className="w-full h-12 rounded-xl border border-gray-200 px-4 font-semibold appearance-none"
                          required
                        >
                          <option value="">Selecciona un tipo...</option>
                          {productTypes?.map((t: ProductType) => (
                            <option key={t.id} value={t.id}>
                              {t.name} - {t.category}
                            </option>
                          ))}
                        </select>
                      </div>
                    </div>

                    <div className="space-y-2">
                      <Label htmlFor="prodLink" className="font-bold">Enlace del Producto</Label>
                      <Input
                        id="prodLink"
                        value={productForm.linkProduct}
                        onChange={(e) => setProductForm({ ...productForm, linkProduct: e.target.value })}
                        className="rounded-xl border-gray-200 h-12"
                      />
                    </div>
                  </div>

                  {/* Columna Derecha */}
                  <div className="space-y-6">
                    <div className="grid grid-cols-3 gap-2">
                      <div className="space-y-2">
                        <Label className="font-bold text-xs uppercase tracking-widest text-gray-500">Fit</Label>
                        <select
                          value={productForm.generalFit}
                          onChange={(e) => setProductForm({ ...productForm, generalFit: e.target.value })}
                          className="w-full h-10 rounded-lg border border-gray-200 px-2 text-sm font-bold"
                        >
                          <option value="SLIM">Slim</option>
                          <option value="REGULAR">Regular</option>
                          <option value="LOOSE">Holgado</option>
                        </select>
                      </div>
                      <div className="space-y-2">
                        <Label className="font-bold text-xs uppercase tracking-widest text-gray-500">Género</Label>
                        <select
                          value={productForm.gender}
                          onChange={(e) => setProductForm({ ...productForm, gender: e.target.value })}
                          className="w-full h-10 rounded-lg border border-gray-200 px-2 text-sm font-bold"
                        >
                          <option value="MALE">Masculino</option>
                          <option value="FEMALE">Femenino</option>
                          <option value="UNISEX">Unisex</option>
                        </select>
                      </div>
                      <div className="space-y-2">
                        <Label className="font-bold text-xs uppercase tracking-widest text-gray-500">Color</Label>
                        <select
                          value={productForm.color}
                          onChange={(e) => setProductForm({ ...productForm, color: e.target.value })}
                          className="w-full h-10 rounded-lg border border-gray-200 px-2 text-sm font-bold"
                        >
                          {["WHITE", "BLACK", "GREY", "BROWN", "BEIGE", "GREEN", "BLUE", "PURPLE", "RED", "ORANGE", "PINK", "YELLOW", "GOLD", "SILVER", "MULTICOLOR", "OTHER"].map(c => (
                            <option key={c} value={c}>{translateEnum(c)}</option>
                          ))}
                        </select>
                      </div>
                    </div>

                    <div className="space-y-2">
                      <Label className="font-bold">Enlaces de Imágenes</Label>
                      <div className="space-y-2 max-h-48 overflow-y-auto pr-2 custom-scrollbar">
                        {(productForm.imageUrls || []).map((url: string, index: number) => (
                          <div key={index} className="flex gap-2">
                             <Input
                              value={url}
                               onChange={(e) => handleImageUrlChange(index, e.target.value)}
                              placeholder={`URL de imagen ${index + 1}`}
                              className="rounded-xl border-gray-200 h-10 text-sm"
                            />
                            {index < (productForm.imageUrls || []).length - 1 && url !== "" && (
                               <button 
                                type="button" 
                                onClick={() => setProductForm({...productForm, imageUrls: (productForm.imageUrls || []).filter((_, i) => i !== index)})}
                                className="p-2 text-red-500 hover:bg-red-50 rounded-lg"
                               >
                                 <X className="h-4 w-4" />
                               </button>
                            )}
                          </div>
                        ))}
                      </div>
                    </div>

                    <div className="space-y-4">
                      <Label className="font-bold">Tags</Label>
                      <div className="flex flex-wrap gap-2 mb-4 p-4 bg-gray-50 rounded-2xl min-h-12 border border-dashed border-gray-200">
                        {(productForm.tagIds || []).length === 0 ? (
                          <span className="text-gray-400 text-sm font-semibold italic">No hay tags seleccionados</span>
                        ) : (
                          (productForm.tagIds || []).map((id: number) => {
                            const tag = tags?.find((t: Tag) => t.id === id);
                            if (!tag) return null;
                            return (
                              <span key={id} className="inline-flex items-center px-3 py-1 rounded-full bg-white border border-gray-200 text-xs font-bold text-[#0A0A0A] shadow-sm animate-fade-in">
                                {tag.name}
                                <button
                                  type="button"
                                  onClick={() => toggleTag(id)}
                                  className="ml-2 text-gray-400 hover:text-red-500 transition-colors"
                                >
                                  <X className="h-3 w-3" />
                                </button>
                              </span>
                            );
                          })
                        )}
                      </div>
                      <div className="grid grid-cols-2 sm:grid-cols-3 gap-2 overflow-y-auto max-h-60 p-2 border rounded-2xl bg-white focus-within:ring-2 focus-within:ring-blue-500/20 transition-all">
                        {tags?.map((t: Tag) => (
                          <button
                            key={t.id}
                            type="button"
                            onClick={() => toggleTag(t.id)}
                            className={`flex items-center justify-between p-2.5 rounded-xl border text-xs font-semibold transition-all ${
                              (productForm.tagIds || []).includes(t.id)
                                ? "bg-blue-50 border-blue-200 text-blue-700 shadow-sm"
                                : "bg-gray-50 border-gray-100 text-[#5F6670] hover:bg-gray-100 hover:border-gray-200"
                            }`}
                          >
                            <span className="truncate mr-2">{t.name}</span>
                            {(productForm.tagIds || []).includes(t.id) && <Check className="h-3 w-3 flex-shrink-0" />}
                          </button>
                        ))}
                      </div>
                    </div>
                  </div>
                </div>

                <Button 
                  type="submit" 
                  className="w-full h-14 rounded-2xl font-black text-xl bg-[#0A0A0A] hover:bg-[#2B2B2B]"
                  disabled={createProductMutation.isPending || updateProductMutation.isPending}
                >
                  {createProductMutation.isPending || updateProductMutation.isPending ? <Loader2 className="animate-spin" /> : (editingProduct ? "Guardar Cambios" : "Agregar Producto")}
                </Button>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

interface ProductCardProps {
  product: ProductManagement;
  onEdit: () => void;
  onDelete: () => void;
  translateEnum: (val: string) => string;
}

function ProductCard({ product, onEdit, onDelete, translateEnum }: ProductCardProps) {
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const images = product.imageUrls && product.imageUrls.length > 0 ? product.imageUrls : [];
  const navigate = useNavigate();

  const nextImage = (e: React.MouseEvent) => {
    e.stopPropagation();
    setCurrentImageIndex((prev) => (prev + 1) % images.length);
  };

  const prevImage = (e: React.MouseEvent) => {
    e.stopPropagation();
    setCurrentImageIndex((prev) => (prev - 1 + images.length) % images.length);
  };

  return (
    <Card className="group rounded-[32px] overflow-hidden border-[#E5E7EB] hover:shadow-xl transition-all h-full flex flex-col">
      <div className="aspect-square bg-[#F3F4F6] relative overflow-hidden">
        {images.length > 0 ? (
          <>
            <img src={images[currentImageIndex]} className="h-full w-full object-cover transition-transform duration-500 group-hover:scale-110" alt={product.name} />
            {images.length > 1 && (
              <div className="absolute inset-0 flex items-center justify-between px-2 opacity-0 group-hover:opacity-100 transition-opacity">
                <button onClick={prevImage} className="p-1 rounded-full bg-white/80 hover:bg-white shadow-sm transition-all">
                  <ChevronLeft className="h-5 w-5" />
                </button>
                <button onClick={nextImage} className="p-1 rounded-full bg-white/80 hover:bg-white shadow-sm transition-all">
                  <ChevronRight className="h-5 w-5" />
                </button>
              </div>
            )}
            <div className="absolute bottom-3 left-1/2 -translate-x-1/2 flex gap-1">
              {images.map((_unused: string, idx: number) => (
                <div key={idx} className={`h-1 rounded-full transition-all ${idx === currentImageIndex ? "w-4 bg-white" : "w-1 bg-white/50"}`} />
              ))}
            </div>
          </>
        ) : (
          <Package className="h-10 w-10 text-[#9CA3AF] absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2" />
        )}
        <div className="absolute top-4 left-4 bg-white/90 backdrop-blur-md px-3 py-1.5 rounded-full flex items-center gap-1.5 shadow-sm border border-white/50">
          <Star className="h-3.5 w-3.5 text-yellow-400 fill-yellow-400" />
          <span className="text-xs font-black text-[#0A0A0A]">{product.rating ? product.rating.toFixed(1) : "0.0"}</span>
        </div>
      </div>
      <CardContent className="p-6 space-y-4 flex-1 flex flex-col">
          <div className="flex-1 space-y-1">
            <div className="flex justify-between items-start gap-4">
              <h3 className="font-bold text-[#0A0A0A] text-lg leading-tight">{product.name}</h3>
              <span className="text-xl font-black text-[#0A0A0A] whitespace-nowrap">${product.price.toLocaleString('es-CO')}</span>
            </div>
            <p className="text-sm font-semibold text-[#5F6670] line-clamp-1">{product.productType.name} - {translateEnum(product.productType.category || "")}</p>
          </div>

          <div className="flex flex-wrap gap-1">
             {product.tags?.slice(0, 3).map((tag) => (
               <span key={tag.id} className="text-[10px] font-bold px-2 py-0.5 bg-gray-100 text-gray-600 rounded-md">
                 {tag.name}
               </span>
             ))}
             {product.tags?.length > 3 && <span className="text-[10px] font-bold px-2 py-0.5 bg-gray-100 text-gray-400 rounded-md">+{product.tags.length - 3}</span>}
          </div>

          <div className="flex gap-2 pt-2">
            <Button variant="outline" size="sm" className="flex-1 rounded-xl h-10 font-bold border-gray-200" onClick={onEdit}>
              <Edit className="h-4 w-4 mr-2" />
              Editar
            </Button>
            <Button
              variant="ghost"
              size="sm"
              className="text-blue-500 hover:bg-blue-50 rounded-xl h-10 w-10"
              title="Ver página del producto"
              onClick={() => navigate(`/productos/${product.id}`, { state: { product: {
                id: product.id,
                name: product.name,
                description: product.description,
                price: product.price,
                productTypeName: product.productType?.name ?? "",
                category: product.productType?.category ?? "",
                generalFit: product.generalFit,
                gender: product.gender,
                color: product.color,
                available: product.available,
                rating: product.rating,
                linkProduct: product.linkProduct,
                imageUrls: product.imageUrls,
                tags: Array.isArray(product.tags) ? product.tags.map((t) => t.name ?? t) : [],
                createdAt: product.createdAt,
                brandName: "",
                brandPictureUrl: "",
                brandIsVerified: false,
              }}})}
            >
              <Eye className="h-4 w-4" />
            </Button>
            <Button variant="ghost" size="sm" className="text-red-500 hover:bg-red-50 rounded-xl h-10 w-10" onClick={onDelete}>
              <Trash2 className="h-4 w-4" />
            </Button>
          </div>
      </CardContent>
    </Card>
  );
}
