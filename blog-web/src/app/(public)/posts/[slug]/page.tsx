
import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
import { getPostBySlug } from "@/services/postService";
import Image from "next/image";

interface SinglePageProps {
    params: {
        slug: string;
    };
}

const SinglePage = async ({ params }: SinglePageProps) => {
    const { slug } = params;
    try {
        const post = await getPostBySlug(slug);
        return (
            <main className="relative min-h-screen bg-background flex flex-col items-center">
                {/* Cover image background with fade overlay */}
                {post.coverImage && (
                    <div className="absolute top-0 left-0 w-full h-[420px] z-0">
                        <Image
                            src={post.coverImage}
                            alt={post.title}
                            fill
                            priority
                            className="object-cover w-full h-full rounded-xl opacity-80"
                        />
                        {/* Fade overlay: image fully visible at top, fades out to transparent at bottom */}
                        <div
                            className="absolute inset-0 w-full h-full pointer-events-none"
                            style={{
                                background: "linear-gradient(to bottom, rgba(0,0,0,0) 60%, var(--background) 100%)"
                            }}
                        />
                    </div>
                )}
                {/* Overlay content on top of the faded image */}
                <section
                    className="relative w-full max-w-5xl mx-auto px-8 flex flex-col items-start z-10"
                    style={{ minHeight: '420px', justifyContent: 'flex-end', display: 'flex', paddingBottom: '2rem', paddingTop: '0', marginTop: '10rem' }}
                >
                    <h1 className="text-4xl md:text-5xl font-extrabold tracking-tight text-white leading-tight mb-0 drop-shadow-xl">
                        {post.title}
                    </h1>
                    <div className="flex items-center gap-4" style={{ marginTop: '3rem' }}>
                        <Avatar className="w-14 h-14 shadow-lg">
                            <AvatarImage src="/profile-picture.png" alt="Gabriel Inacio" />
                            <AvatarFallback>GI</AvatarFallback>
                        </Avatar>
                        <div>
                            <span className="font-semibold text-lg text-white drop-shadow">Gabriel Inacio</span>
                            <div className="text-xs text-white/80 mt-1 drop-shadow">{post.creationDate}</div>
                        </div>
                    </div>
                </section>
                {/* Main content below the header */}
                <section className="relative w-full max-w-5xl mx-auto px-8 pb-16 z-10" style={{ marginTop: '3rem' }}>
                    <article className="prose prose-lg prose-zinc dark:prose-invert max-w-none text-foreground rounded-xl shadow-xl p-8 -mt-16 bg-transparent">
                        <div dangerouslySetInnerHTML={{ __html: post.content }} />
                    </article>
                </section>
            </main>
        );
    } catch (err) {
        const error = err instanceof Error ? err.message : "Unknown error";
        return (
            <main className="min-h-[40vh] flex flex-col items-center justify-center bg-gradient-to-br from-background via-muted/40 to-background">
                <section className="w-full max-w-lg p-8 rounded-xl shadow-xl bg-white/90 dark:bg-zinc-900/90 backdrop-blur-lg">
                    <h1 className="text-2xl font-bold tracking-tight mb-2 text-destructive">Post Not Found</h1>
                    <p className="text-muted-foreground text-base mt-4">{error}</p>
                </section>
            </main>
        );
    }
};

export default SinglePage;
