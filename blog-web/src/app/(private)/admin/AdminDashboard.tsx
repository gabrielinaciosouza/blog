import React from "react";
import Post from "@/models/post";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";
import { useRouter } from "next/navigation";
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

export default function AdminDashboard({
    posts,
    deletedPosts,
    isLoading,
    handleDelete,
    handleCreateNewPost,
    page,
    hasPrev,
    hasNext,
    totalCount,
    pageSize,
}: AdminDashboardProps) {
    const router = useRouter();
    const [activeMenu, setActiveMenu] = React.useState("Active Posts");

    return (
        <div className="min-h-screen bg-muted/60 flex rounded-xl">
            {/* Sidebar */}
            <aside className="hidden md:flex flex-col w-64 h-screen flex-shrink-0 bg-card border-r border-border p-6 gap-2 rounded-xl rounded-tr-none rounded-br-none">
                <div className="mb-8 flex items-center gap-2">
                    <img src="/profile-picture.png" alt="Admin" className="w-12 h-12 rounded-full border border-border shadow-sm" />
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
            {/* Main Content */}
            <main className="flex-1 p-4 md:p-10 xl:p-16">
                <div className="flex items-center justify-between mb-8">
                    <div className="flex items-center gap-4">
                        <h2 className="text-2xl font-bold text-foreground">{activeMenu}</h2>
                    </div>
                    <Button onClick={handleCreateNewPost} variant="default" className="px-4 py-1.5 text-sm font-medium shadow-sm ml-auto">
                        + New Post
                    </Button>
                </div>
                <div className="bg-card rounded-xl shadow p-6 border border-border w-full">
                    {isLoading && (
                        <div className="flex justify-center items-center py-10">
                            <Loader2 className="animate-spin w-10 h-10 text-primary" />
                        </div>
                    )}
                    {activeMenu === "Active Posts" && (
                        posts.length > 0 ? (
                            <div className="flex flex-col gap-4">
                                {posts.map((post: Post) => (
                                    <PostTile key={post.postId} post={post} onDelete={() => handleDelete(post.slug)} />
                                ))}
                            </div>
                        ) : (
                            <div className="text-center text-xl text-muted-foreground py-16">No posts available</div>
                        )
                    )}
                    {activeMenu === "Deleted Posts" && (
                        deletedPosts.length > 0 ? (
                            <div className="flex flex-col gap-4">
                                {deletedPosts.map((post: Post) => (
                                    <PostTile key={post.postId} post={post} />
                                ))}
                            </div>
                        ) : (
                            <div className="text-center text-xl text-muted-foreground py-16">No deleted posts</div>
                        )
                    )}
                    {/* Add more menu content as needed */}
                </div>
            </main>
        </div>
    );
}
