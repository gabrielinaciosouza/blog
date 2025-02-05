import React from "react";
import styles from "./categoryList.module.css"
import Link from "next/link";
import {Category} from "@/models/category";
import Image from "next/image";

const CategoryList = () => {
    const categories = [
        new Category("id1", "Java", "https://cdn.worldvectorlogo.com/logos/java.svg", "#FF000066"),
        new Category("id2", "Career", "https://nfil.net/wp-content/uploads/2024/01/Career-Readiness.jpg", "#0000FF66"),
        new Category("id3", "Databases", "https://www.bruker.com/pl/products-and-solutions/infrared-and-raman/opus-spectroscopy-software/database/_jcr_content/teaserImage.coreimg.jpeg/1594725147750/database-icon.jpeg", "#00800066"),
        new Category("id4", "Architecture", "https://external-preview.redd.it/i7layUrGWM3wMxy6gn6n0B2mpUgHD1pilLOg-btC5RE.jpg?width=640&crop=smart&auto=webp&s=bc339e9c316d1d3341cd92155dc03459ac1993b6", "#FFFF0066"),
    ]
    return (
        <div className={styles.container}>
            <div className={styles.headline}>
                <h1 className={styles.title}>Popular Categories</h1>
                <Link href="/" className={styles.categoriesLink}>Show more</Link>
            </div>
            <div className={styles.categories}>
            {
                    categories.map(category => (

                        <Link href={`/blog?category=${category.categoryName}`}
                              className={styles.category}
                              style={{backgroundColor: category.categoryColor, width: `${100 / categories.length}%`}}
                              key={category.categoryId}>
                            <Image src={category.categoryImageUrl} alt="" width={32} height={32}
                                   className={styles.image}/>
                            {category.categoryName}
                        </Link>
                    ))
                }

            </div>
        </div>
    )
}

export default CategoryList;
