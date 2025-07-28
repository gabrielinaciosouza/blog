import PostList from "@/components/postList/PostList";
import Featured from "@/components/featured/Featured";

const Home = () => {
    return (
        <main className="w-full min-h-screen bg-background flex flex-col items-center py-8 px-4 md:px-8 lg:px-32">
            <section className="w-full max-w-8xl mb-12">
                <Featured />
            </section>
            <section className="w-full max-w-8xl flex justify-center">
                <PostList />
            </section>
        </main>
    );
}

export default Home;