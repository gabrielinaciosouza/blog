import React from "react";

const Loading: React.FC = () => {
    return (
        <div className="fixed inset-0 z-50 flex flex-col items-center justify-center bg-black/60 gap-4">
            <span className="inline-block animate-spin rounded-full border-4 border-primary border-t-transparent h-12 w-12" />
            <p className="text-base text-muted-foreground font-medium">Loading...</p>
        </div>
    );
};

export default Loading;