<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Кофейня — кофе, книги и кино</title>

    <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/home.css'/>">
</head>
<body>

<c:import url="/jsp/fragments/role-header.jsp"/>

<main class="home-page">

    <section class="home-slider" aria-label="Акции и ближайшее мероприятие">
        <div class="home-slides" id="home-slides">

            <c:forEach var="promotion" items="${promotions}" varStatus="loop">
                <article class="home-slide ${loop.first ? 'is-active' : ''}">

                    <c:if test="${not empty promotion.imageUrl}">
                        <img class="home-slide-background"
                             src="<c:url value='${promotion.imageUrl}'/>"
                             alt="<c:out value='${promotion.title}'/>">
                    </c:if>

                    <div class="home-slide-content">
                        <p class="home-eyebrow">Специальное предложение</p>

                        <h1><c:out value="${promotion.title}"/></h1>

                        <p class="home-slide-description">
                            <c:out value="${promotion.shortDescription}"/>
                        </p>

                        <a class="home-button" href="<c:url value='/promotions'/>">
                            Смотреть акции
                        </a>
                    </div>
                </article>
            </c:forEach>

            <c:if test="${not empty nextEvent}">
                <article class="home-slide ${empty promotions ? 'is-active' : ''}">

                    <c:if test="${not empty nextEvent.imageUrl}">
                        <img class="home-slide-background"
                             src="<c:url value='${nextEvent.imageUrl}'/>"
                             alt="<c:out value='${nextEvent.title}'/>">
                    </c:if>

                    <div class="home-slide-content">
                        <p class="home-eyebrow">Ближайшее мероприятие</p>

                        <h2><c:out value="${nextEvent.title}"/></h2>

                        <p class="home-event-date">
                            <c:out value="${nextEvent.formattedDate}"/>
                            ·
                            <c:out value="${nextEvent.formattedTime}"/>
                        </p>

                        <p class="home-slide-description">
                            <c:out value="${nextEvent.description}"/>
                        </p>

                        <a class="home-button" href="<c:url value='/events'/>">
                            Смотреть афишу
                        </a>
                    </div>
                </article>
            </c:if>

            <c:if test="${empty promotions and empty nextEvent}">
                <article class="home-slide is-active home-slide-welcome">
                    <div class="home-slide-content">
                        <p class="home-eyebrow">Кофе · книги · кино</p>

                        <h1>Место, где истории оживают</h1>

                        <p class="home-slide-description">
                            Заглядывай за любимым кофе и оставайся на уютный вечер.
                        </p>

                        <a class="home-button" href="<c:url value='/menu'/>">
                            Перейти в меню
                        </a>
                    </div>
                </article>
            </c:if>

        </div>

        <button class="slider-arrow slider-arrow-prev"
                type="button"
                aria-label="Предыдущий слайд">
            ‹
        </button>

        <button class="slider-arrow slider-arrow-next"
                type="button"
                aria-label="Следующий слайд">
            ›
        </button>

        <div class="slider-dots" id="slider-dots"></div>
    </section>

    <section class="home-links" aria-label="Разделы сайта">
        <a class="home-link-card" href="<c:url value='/menu'/>">
            <span class="home-link-icon" aria-hidden="true">☕</span>
            <h2>Меню</h2>
            <p>Выбери напиток к своему вечеру</p>
            <span class="home-link-more">Посмотреть меню →</span>
        </a>

        <a class="home-link-card" href="<c:url value='/events'/>">
            <span class="home-link-icon" aria-hidden="true">🎬</span>
            <h2>Мероприятия</h2>
            <p>Кино, книжные клубы и встречи</p>
            <span class="home-link-more">Открыть афишу →</span>
        </a>

        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <a class="home-link-card" href="<c:url value='/profile'/>">
                    <span class="home-link-icon" aria-hidden="true">♡</span>
                    <h2>Твой профиль</h2>
                    <p>Переходи в личный кабинет</p>
                    <span class="home-link-more">Открыть профиль →</span>
                </a>
            </c:when>

            <c:otherwise>
                <a class="home-link-card" href="<c:url value='/sign-up'/>">
                    <span class="home-link-icon" aria-hidden="true">✦</span>
                    <h2>Присоединяйся</h2>
                    <p>Создай аккаунт, чтобы участвовать в жизни кофейни</p>
                    <span class="home-link-more">Регистрация →</span>
                </a>
            </c:otherwise>
        </c:choose>
    </section>

    <section class="home-about" id="about">
        <div class="home-about-intro">
            <p class="home-about-eyebrow">О кофейне</p>
            <h2>Больше, чем просто кофе</h2>

            <p>
                Мы придумали кофейню, где любимые напитки встречаются
                с хорошими историями. Здесь можно заглянуть за кофе,
                познакомиться с новой книгой или провести вечер за просмотром фильма.
            </p>

            <p>
                Мы устраиваем небольшие мероприятия максимум для 15 гостей,
                чтобы каждый мог почувствовать себя частью уютной компании.
                А некоторые тематические вечера будут начинаться с загадки,
                которая подскажет, что ждёт гостей.
            </p>

            <a class="home-about-link" href="<c:url value='/events'/>">
                Посмотреть ближайшие мероприятия →
            </a>
        </div>

        <div class="home-idea-cards">
            <article class="home-idea-card">
                <span class="home-idea-icon" aria-hidden="true">☕</span>
                <h3>Любимый кофе</h3>
                <p>
                    Классические и авторские напитки, которые можно выбрать
                    под настроение.
                </p>
            </article>

            <article class="home-idea-card">
                <span class="home-idea-icon" aria-hidden="true">📚</span>
                <h3>Общие интересы</h3>
                <p>
                    Книжные клубы и встречи для тех, кому нравится обсуждать
                    прочитанное и делиться впечатлениями.
                </p>
            </article>

            <article class="home-idea-card">
                <span class="home-idea-icon" aria-hidden="true">🎬</span>
                <h3>Вечера с загадкой</h3>
                <p>
                    Кинопросмотры и тематические события, название которых
                    можно попробовать разгадать заранее.
                </p>
            </article>
        </div>
    </section>

</main>

<c:import url="/jsp/fragments/footer.jsp"/>

<script>
    const slides = Array.from(document.querySelectorAll(".home-slide"));
    const dotsContainer = document.getElementById("slider-dots");
    const previousButton = document.querySelector(".slider-arrow-prev");
    const nextButton = document.querySelector(".slider-arrow-next");

    let activeSlide = slides.findIndex(slide =>
        slide.classList.contains("is-active")
    );

    if (activeSlide < 0 && slides.length > 0) {
        activeSlide = 0;
        slides[0].classList.add("is-active");
    }

    function showSlide(index) {
        if (slides.length === 0) {
            return;
        }

        activeSlide = (index + slides.length) % slides.length;

        slides.forEach((slide, slideIndex) => {
            slide.classList.toggle("is-active", slideIndex === activeSlide);
        });

        document.querySelectorAll(".slider-dot").forEach((dot, dotIndex) => {
            dot.classList.toggle("is-active", dotIndex === activeSlide);
        });
    }

    slides.forEach((slide, index) => {
        const dot = document.createElement("button");
        dot.type = "button";
        dot.className = "slider-dot";
        dot.setAttribute("aria-label", "Показать слайд " + (index + 1));
        dot.addEventListener("click", () => showSlide(index));
        dotsContainer.appendChild(dot);
    });

    showSlide(activeSlide);

    previousButton.addEventListener("click", () => {
        showSlide(activeSlide - 1);
    });

    nextButton.addEventListener("click", () => {
        showSlide(activeSlide + 1);
    });

    if (slides.length < 2) {
        previousButton.hidden = true;
        nextButton.hidden = true;
        dotsContainer.hidden = true;
    } else {
        window.setInterval(() => {
            showSlide(activeSlide + 1);
        }, 5000);
    }
</script>

</body>
</html>