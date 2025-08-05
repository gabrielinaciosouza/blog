import React from "react";
import Image from "next/image";
import Post from "@/models/post";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";
import PostTile from "@/components/postTile/PostTile";
import { Loader2 } from "lucide-react";

const menuItems = [
    { label: "Dashboard", icon: "🏠" },
    { label: "Active Posts", icon: "📝" },
    { label: "Deleted Posts", icon: "🗑️" },
    { label: "Settings", icon: "⚙️" },
];

type AdminDashboardProps = {
    posts: Post[];
    deletedPosts: Post[];
    isLoading: boolean;
    handleDelete: (slug: string) => void;
    handleCreateNewPost: () => void;
    page: number;
    hasPrev: boolean;
    hasNext: boolean;
    totalCount: number;
    pageSize: number;
};

import { useRouter } from "next/navigation";

export default function AdminDashboard({
    posts,
    deletedPosts,
    isLoading,
    handleDelete,
    handleCreateNewPost,
}: AdminDashboardProps) {
    const router = useRouter();
    const [activeMenu, setActiveMenu] = React.useState("Active Posts");
    const [sidebarOpen, setSidebarOpen] = React.useState(false);

    const handleEdit = (slug: string) => {
        router.push(`/create-post?slug=${encodeURIComponent(slug)}`);
    };

    return (
        <div className="min-h-screen bg-muted/60 flex items-stretch rounded-xl relative">


            <aside className="hidden xl:flex flex-col w-64 flex-shrink-0 bg-card border-r border-border p-6 gap-2 rounded-xl rounded-tr-none rounded-br-none">
                <div className="mb-8 flex items-center gap-2">
                    <Image src="/profile-picture.png" alt="Admin" width={48} height={48} className="w-12 h-12 rounded-full border border-border shadow-sm" />
                    <div>
                        <h1 className="text-lg font-semibold text-foreground">Admin</h1>
                        <p className="text-muted-foreground text-xs">Gabriel</p>
                    </div>
                </div>
                {menuItems.map((item) => (
                    <button
                        key={item.label}
                        className={cn(
                            "flex items-center gap-3 px-4 py-2 rounded-lg text-sm font-medium transition-colors",
                            activeMenu === item.label
                                ? "bg-primary text-primary-foreground"
                                : "hover:bg-muted text-muted-foreground"
                        )}
                        onClick={() => setActiveMenu(item.label)}
                    >
                        <span>{item.icon}</span>
                        {item.label}
                    </button>
                ))}
            </aside>


            {sidebarOpen && (
                <div className="absolute top-0 left-0 w-full h-full z-40 flex">
                    {/* Overlay */}
                    <div className="absolute inset-0 bg-black/40" onClick={() => setSidebarOpen(false)} />
                    <aside className="relative flex flex-col w-64 bg-card border-r border-border p-6 gap-2 rounded-xl rounded-tr-none rounded-br-none h-full z-50 shadow-lg animate-slideInLeft">
                        <button
                            className="absolute top-4 right-4 text-muted-foreground"
                            aria-label="Close sidebar"
                            onClick={() => setSidebarOpen(false)}
                        >
                            <svg width="24" height="24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><line x1="18" y1="6" x2="6" y2="18" /><line x1="6" y1="6" x2="18" y2="18" /></svg>
                        </button>
                        <div className="mb-8 flex items-center gap-2">
                            <Image src="/profile-picture.png" alt="Admin" width={48} height={48} className="w-12 h-12 rounded-full border border-border shadow-sm" />
                            <div>
                                <h1 className="text-lg font-semibold text-foreground">Admin</h1>
                                <p className="text-muted-foreground text-xs">Gabriel</p>
                            </div>
                        </div>
                        {menuItems.map((item) => (
                            <button
                                key={item.label}
                                className={cn(
                                    "flex items-center gap-3 px-4 py-2 rounded-lg text-sm font-medium transition-colors",
                                    activeMenu === item.label
                                        ? "bg-primary text-primary-foreground"
                                        : "hover:bg-muted text-muted-foreground"
                                )}
                                onClick={() => {
                                    setActiveMenu(item.label);
                                    setSidebarOpen(false);
                                }}
                            >
                                <span>{item.icon}</span>
                                {item.label}
                            </button>
                        ))}
                    </aside>
                </div>
            )}

            <main className="flex-1 flex flex-col h-full p-4 md:p-10 xl:p-16">
                <div className="flex items-center justify-between mb-8">
                    <div className="flex items-center gap-4">
                        <button
                            className="xl:hidden bg-card p-2 rounded-lg shadow border border-border mr-2"
                            aria-label="Open sidebar"
                            onClick={() => setSidebarOpen(true)}
                        >
                            <span className="sr-only">Open sidebar</span>
                            <svg width="24" height="24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><line x1="3" y1="12" x2="21" y2="12" /><line x1="3" y1="6" x2="21" y2="6" /><line x1="3" y1="18" x2="21" y2="18" /></svg>
                        </button>
                        <h2 className="text-2xl font-bold text-foreground ml-0">{activeMenu}</h2>
                    </div>
                    <Button onClick={() => router.push('/create-post')} variant="default" className="px-4 py-1.5 text-sm font-medium shadow-sm ml-auto">
                        + New Post
                    </Button>
                </div>
                <div className="bg-card rounded-xl shadow p-6 border border-border w-full flex-1 flex flex-col">
                    {isLoading && (
                        <div className="flex justify-center items-center py-10">
                            <Loader2 className="animate-spin w-10 h-10 text-primary" />
                        </div>
                    )}
                    {activeMenu === "Active Posts" && (
                        posts.length > 0 ? (
                            <div className="flex flex-col gap-4 flex-1">
                                {posts.map((post: Post) => (
                                    <PostTile
                                        key={post.postId}
                                        post={post}
                                        onEdit={() => handleEdit(post.slug)}
                                        onDelete={() => handleDelete(post.slug)}
                                    />
                                ))}
                            </div>
                        ) : (
                            <div className="text-center text-xl text-muted-foreground py-16 flex-1 flex items-center justify-center">No posts available</div>
                        )
                    )}
                    {activeMenu === "Deleted Posts" && (
                        deletedPosts.length > 0 ? (
                            <div className="flex flex-col gap-4 flex-1">
                                {deletedPosts.map((post: Post) => (
                                    <PostTile key={post.postId} post={post} />
                                ))}
                            </div>
                        ) : (
                            <div className="text-center text-xl text-muted-foreground py-16 flex-1 flex items-center justify-center">No deleted posts</div>
                        )
                    )}
                </div>
            </main>
        </div>
    );
}
