package org.infobip.mobile.messaging.chat.view.styles

import android.content.Context
import android.content.res.Resources.Theme
import android.content.res.TypedArray
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.*
import androidx.core.content.res.getBooleanOrThrow
import androidx.core.graphics.ColorUtils
import com.google.android.material.appbar.MaterialToolbar
import org.infobip.mobile.messaging.api.chat.WidgetInfo
import org.infobip.mobile.messaging.chat.R
import org.infobip.mobile.messaging.chat.utils.*
import org.infobip.mobile.messaging.logging.MobileMessagingLogger

data class InAppChatToolbarStyle(
    @ColorInt val toolbarBackgroundColor: Int,
    @ColorInt val statusBarBackgroundColor: Int,
    @DrawableRes val navigationIcon: Int,
    @ColorInt val navigationIconTint: Int,
    @StyleRes val titleTextAppearance: Int? = null,
    @ColorInt val titleTextColor: Int,
    val titleText: String? = null,
    @StringRes val titleTextRes: Int? = null,
    val isTitleCentered: Boolean? = null,
    @StyleRes val subtitleTextAppearance: Int? = null,
    @ColorInt val subtitleTextColor: Int,
    val subtitleText: String? = null,
    @StringRes val subtitleTextRes: Int? = null,
    val isSubtitleCentered: Boolean? = null,
    val isIbDefaultTheme: Boolean
) {

    companion object {

        private const val RES_ID_CHAT_VIEW_TITLE = "ib_in_app_chat_view_title"

        internal operator fun invoke(@AttrRes attr: Int, context: Context): InAppChatToolbarStyle {
            val theme = context.theme

            //load deprecated attributes
            val deprecatedToolbarBackgroundColor = context.resolveThemeColor(R.attr.colorPrimary)
            val deprecatedStatusBarBackgroundColor = context.resolveThemeColor(R.attr.colorPrimaryDark)
            val deprecatedTitleTextColor = context.resolveThemeColor(R.attr.titleTextColor)
            val deprecatedNavigationIconTint = context.resolveThemeColor(R.attr.colorControlNormal)
            val deprecatedTitleResId: Int? = runCatching {
                context.resources.getIdentifier(RES_ID_CHAT_VIEW_TITLE, "string", context.applicationContext.packageName).takeIfDefined()
            }.onFailure {
                MobileMessagingLogger.e("Can't load resource: $RES_ID_CHAT_VIEW_TITLE", it)
            }.getOrNull()
            val deprecatedTitle = context.getString(deprecatedTitleResId ?: R.string.ib_chat_view_title)

            //load new style values
            var newToolbarBackgroundColor: Int? = null
            var newStatusBarBackgroundColor: Int? = null
            var newNavigationIcon: Int? = null
            var newNavigationIconTint: Int? = null
            var newTitleTextAppearance: Int? = null
            var newTitleTextColor: Int? = null
            var newTitleText: String? = null
            var newTitleTextRes: Int? = null
            var newIsTitleCentered: Boolean? = null
            var newSubtitleTextAppearance: Int? = null
            var newSubtitleTextColor: Int? = null
            var newSubtitleText: String? = null
            var newSubtitleTextRes: Int? = null
            var newIsSubtitleCentered: Boolean? = null

            val typedValue = TypedValue()
            theme.resolveAttribute(attr, typedValue, true)
            if (typedValue.data != 0) {
                val typedArray: TypedArray = theme.obtainStyledAttributes(typedValue.data, R.styleable.InAppChatToolbarViewStyleable)
                newToolbarBackgroundColor = typedArray.getColor(R.styleable.InAppChatToolbarViewStyleable_ibChatToolbarBackgroundColor, 0).takeIfDefined()
                newStatusBarBackgroundColor = typedArray.getColor(R.styleable.InAppChatToolbarViewStyleable_ibChatStatusBarBackgroundColor, 0).takeIfDefined()
                newNavigationIcon = typedArray.getResourceId(R.styleable.InAppChatToolbarViewStyleable_ibChatNavigationIcon, 0).takeIfDefined()
                newNavigationIconTint = typedArray.getColor(R.styleable.InAppChatToolbarViewStyleable_ibChatNavigationIconTint, 0).takeIfDefined()
                newTitleTextAppearance = typedArray.getResourceId(R.styleable.InAppChatToolbarViewStyleable_ibChatTitleTextAppearance, 0).takeIfDefined()
                newTitleTextColor = typedArray.getColor(R.styleable.InAppChatToolbarViewStyleable_ibChatTitleTextColor, 0).takeIfDefined()
                val newTitleTextNonResource = typedArray.getNonResourceString(R.styleable.InAppChatToolbarViewStyleable_ibChatTitleText)
                if (newTitleTextNonResource != null) {
                    newTitleText = newTitleTextNonResource
                } else {
                    newTitleText = typedArray.getString(R.styleable.InAppChatToolbarViewStyleable_ibChatTitleText)
                    newTitleTextRes = typedArray.getResourceId(R.styleable.InAppChatToolbarViewStyleable_ibChatTitleText, 0).takeIfDefined()
                }
                if (newTitleTextRes != null && newTitleText == null){
                    newTitleText = context.getString(newTitleTextRes)
                }
                newIsTitleCentered = runCatching { typedArray.getBooleanOrThrow(R.styleable.InAppChatToolbarViewStyleable_ibChatTitleCentered) }.getOrNull()
                newSubtitleTextAppearance = typedArray.getResourceId(R.styleable.InAppChatToolbarViewStyleable_ibChatSubtitleTextAppearance, 0).takeIfDefined()
                newSubtitleTextColor = typedArray.getColor(R.styleable.InAppChatToolbarViewStyleable_ibChatSubtitleTextColor, 0).takeIfDefined()
                val newSubtitleTextNonResource = typedArray.getNonResourceString(R.styleable.InAppChatToolbarViewStyleable_ibChatSubtitleText)
                if (newSubtitleTextNonResource != null) {
                    newSubtitleText = newSubtitleTextNonResource
                } else {
                    newSubtitleText = typedArray.getString(R.styleable.InAppChatToolbarViewStyleable_ibChatSubtitleText)
                    newSubtitleTextRes = typedArray.getResourceId(R.styleable.InAppChatToolbarViewStyleable_ibChatSubtitleText, 0).takeIfDefined()
                }
                if (newSubtitleTextRes != null && newSubtitleText == null){
                    newSubtitleText = context.getString(newSubtitleTextRes)
                }
                newIsSubtitleCentered = runCatching { typedArray.getBooleanOrThrow(R.styleable.InAppChatToolbarViewStyleable_ibChatSubtitleCentered) }.getOrNull()
                typedArray.recycle()
            }

            return InAppChatToolbarStyle(
                toolbarBackgroundColor = newToolbarBackgroundColor ?: deprecatedToolbarBackgroundColor ?: Color.BLACK,
                statusBarBackgroundColor = newStatusBarBackgroundColor ?: deprecatedStatusBarBackgroundColor ?: Color.BLACK,
                navigationIcon = newNavigationIcon ?: R.drawable.ic_chat_arrow_back,
                navigationIconTint = newNavigationIconTint ?: deprecatedNavigationIconTint ?: Color.WHITE,
                titleTextAppearance = newTitleTextAppearance,
                titleTextColor = newTitleTextColor ?: deprecatedTitleTextColor ?: Color.WHITE,
                titleText = newTitleText ?: deprecatedTitle,
                titleTextRes = newTitleTextRes ?: deprecatedTitleResId,
                isTitleCentered = newIsTitleCentered,
                subtitleTextAppearance = newSubtitleTextAppearance,
                subtitleTextColor = newSubtitleTextColor ?: deprecatedTitleTextColor ?: Color.WHITE,
                subtitleText = newSubtitleText,
                subtitleTextRes = newSubtitleTextRes,
                isSubtitleCentered = newIsSubtitleCentered,
                isIbDefaultTheme = theme.isIbDefaultTheme()
            )

        }

        private fun calculatePrimaryDarkColor(primaryColor: Int): Int {
            return ColorUtils.blendARGB(primaryColor, Color.BLACK, 0.2f)
        }

        private fun Theme?.isMMBaseTheme(): Boolean {
            return this?.let {
                listOf(
                    resolveThemeColor(R.attr.colorPrimary),
                    resolveThemeColor(R.attr.colorPrimaryDark),
                    resolveThemeColor(R.attr.colorControlNormal),
                    resolveThemeColor(R.attr.titleTextColor),
                ).all { it == Color.BLACK }
            } ?: false
        }

        private fun prepareStyle(@AttrRes attr: Int, context: Context, widgetInfo: WidgetInfo?): InAppChatToolbarStyle {
            //theme config
            var style = invoke(attr, context)
            val theme = context.theme

            @ColorInt
            val colorPrimary = widgetInfo?.colorPrimary
            @ColorInt
            val backgroundColor = widgetInfo?.colorBackground

            if (style.isIbDefaultTheme) { //if it is IB default theme apply widget color automatically to all components
                if (colorPrimary != null) {
                    style = style.copy(
                        toolbarBackgroundColor = colorPrimary,
                        statusBarBackgroundColor = calculatePrimaryDarkColor(colorPrimary)
                    )
                }
                if (backgroundColor != null) {
                    style = style.copy(
                        titleTextColor = backgroundColor,
                        subtitleTextColor = backgroundColor,
                        navigationIconTint = backgroundColor
                    )
                }
            } else { //if it is theme provided by integrator apply widget color only to components which are not defined by integrator
                val isBaseTheme = theme.isMMBaseTheme()
                val deprecatedColorPrimaryDefined = theme.isAttributePresent(R.attr.colorPrimary)
                val applyWidgetColorPrimary = (isBaseTheme || !deprecatedColorPrimaryDefined)

                val newBackgroundColorDefined = theme.isAttributePresent(
                    R.styleable.InAppChatToolbarViewStyleable_ibChatToolbarBackgroundColor,
                    attr,
                    R.styleable.InAppChatToolbarViewStyleable
                )
                if (applyWidgetColorPrimary && !newBackgroundColorDefined && colorPrimary != null) {
                    style = style.copy(toolbarBackgroundColor = colorPrimary)
                }
                val newStatusBarBackgroundColorDefined = theme.isAttributePresent(
                    R.styleable.InAppChatToolbarViewStyleable_ibChatStatusBarBackgroundColor,
                    attr,
                    R.styleable.InAppChatToolbarViewStyleable
                )
                if (applyWidgetColorPrimary && !newStatusBarBackgroundColorDefined && colorPrimary != null) {
                    style = style.copy(statusBarBackgroundColor = calculatePrimaryDarkColor(colorPrimary))
                }

                val deprecatedTitleTextColorDefined = theme.isAttributePresent(R.attr.titleTextColor)
                val applyWidgetTitleTextColor = (isBaseTheme || !deprecatedTitleTextColorDefined)

                val newTitleTextColorDefined = theme.isAttributePresent(R.styleable.InAppChatToolbarViewStyleable_ibChatTitleTextColor, attr, R.styleable.InAppChatToolbarViewStyleable)
                if (applyWidgetTitleTextColor && !newTitleTextColorDefined && backgroundColor != null){
                    style = style.copy(titleTextColor = backgroundColor)
                }
                val newSubtitleTextColorDefined = theme.isAttributePresent(R.styleable.InAppChatToolbarViewStyleable_ibChatSubtitleTextColor, attr, R.styleable.InAppChatToolbarViewStyleable)
                if (applyWidgetTitleTextColor && !newSubtitleTextColorDefined && backgroundColor != null){
                    style = style.copy(subtitleTextColor = backgroundColor)
                }
                val newNavigationIconTintDefined = theme.isAttributePresent(R.styleable.InAppChatToolbarViewStyleable_ibChatNavigationIconTint, attr, R.styleable.InAppChatToolbarViewStyleable)
                if (applyWidgetTitleTextColor && !newNavigationIconTintDefined && backgroundColor != null){
                    style = style.copy(navigationIconTint = backgroundColor)
                }
            }

            if (style.titleText?.isBlank() == true && widgetInfo != null){
                style = style.copy(titleText = widgetInfo.getTitle(), titleTextRes = null);
            }

            return style
        }

        /**
         * Creates [InAppChatToolbarStyle] from android theme "IB_AppTheme.Chat" defined by integrator and [WidgetInfo] livechat widget configuration.
         * If "IB_AppTheme.Chat" does not exists in application, then online fetched [WidgetInfo] is used.
         * If [WidgetInfo] could not be fetched, then default IB theme "IB_ChatDefaultTheme.Styled" as last option.
         * Priority: IB_AppTheme.Chat > [WidgetInfo] > IB_ChatDefaultTheme
         */
        @JvmStatic
        fun createChatToolbarStyle(context: Context, widgetInfo: WidgetInfo?): InAppChatToolbarStyle = prepareStyle(R.attr.ibChatToolbarStyle, context, widgetInfo)

        /**
         * Creates [InAppChatToolbarStyle] from android theme "IB_AppTheme.Chat" defined by integrator and [WidgetInfo] livechat widget configuration.
         * If "IB_AppTheme.Chat" does not exists in application, then online fetched [WidgetInfo] is used.
         * If [WidgetInfo] could not be fetched, then default IB theme "IB_ChatDefaultTheme.Styled" as last option.
         * Priority: IB_AppTheme.Chat > [WidgetInfo] > IB_ChatDefaultTheme
         */
        @JvmStatic
        fun createChatAttachmentStyle(context: Context, widgetInfo: WidgetInfo?): InAppChatToolbarStyle = prepareStyle(R.attr.ibChatAttachmentToolbarStyle, context, widgetInfo)
    }

}

fun InAppChatToolbarStyle.apply(toolbar: MaterialToolbar?){
    toolbar?.let {
        it.setNavigationIcon(navigationIcon)
        it.setNavigationIconTint(navigationIconTint)
        it.setBackgroundColor(toolbarBackgroundColor)
        it.title = titleText ?: ""
        titleTextAppearance?.let { appearance -> toolbar.setTitleTextAppearance(toolbar.context, appearance) }
        it.setTitleTextColor(titleTextColor)
        isTitleCentered?.let { isCentered -> it.isTitleCentered = isCentered }
        it.subtitle = subtitleText
        subtitleTextAppearance?.let { appearance -> toolbar.setSubtitleTextAppearance(toolbar.context, appearance) }
        it.setSubtitleTextColor(subtitleTextColor)
        isSubtitleCentered?.let { isCentered -> it.isSubtitleCentered = isCentered }
    }
}